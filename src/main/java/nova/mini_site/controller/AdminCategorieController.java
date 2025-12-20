package nova.mini_site.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import nova.mini_site.model.dto.CategorieDto;
import nova.mini_site.model.entites.Categorie;
import nova.mini_site.service.ServiceCategorie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategorieController {

    private final ServiceCategorie serviceCategorie;

    public AdminCategorieController(ServiceCategorie serviceCategorie) {
        this.serviceCategorie = serviceCategorie;
    }
    @Operation(summary = "Créer un categorie")
    @PostMapping
    public ResponseEntity<Categorie> creer(@Valid @RequestBody CategorieDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(serviceCategorie.creer(dto));
    }

    @Operation(summary = "modifier un categorie")
    @PutMapping("/{id}")
    public ResponseEntity<Categorie> modifier(
            @PathVariable Long id,
            @Valid @RequestBody CategorieDto dto) {

        return ResponseEntity.ok(serviceCategorie.modifier(id, dto));
    }

    @Operation(summary = "supprimer un categorie")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        serviceCategorie.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
