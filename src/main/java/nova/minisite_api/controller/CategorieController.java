package nova.minisite_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import nova.minisite_api.model.dto.CategorieDto;
import nova.minisite_api.model.entites.Categorie;
import nova.minisite_api.service.ServiceCategorie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Catégories")
public class CategorieController {

    private final ServiceCategorie serviceCategorie;

    public CategorieController(ServiceCategorie serviceCategorie) {
        this.serviceCategorie = serviceCategorie;
    }

    // --- ACCÈS PUBLIC ---

    @Operation(summary = "Lister toutes les catégories")
    @GetMapping
    public ResponseEntity<List<Categorie>> lister() {
        return ResponseEntity.ok(serviceCategorie.lister());
    }

    // --- ACCÈS ADMIN UNIQUEMENT ---

    @Operation(summary = "Créer une catégorie (Admin)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categorie> creer(@Valid @RequestBody CategorieDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(serviceCategorie.creer(dto));
    }

    @Operation(summary = "Modifier une catégorie (Admin)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categorie> modifier(
            @PathVariable Long id,
            @Valid @RequestBody CategorieDto dto) {
        return ResponseEntity.ok(serviceCategorie.modifier(id, dto));
    }

    @Operation(summary = "Supprimer une catégorie (Admin)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        serviceCategorie.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}