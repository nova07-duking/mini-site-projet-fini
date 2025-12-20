package nova.minisite_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import nova.minisite_api.model.dto.ProduitDto;
import nova.minisite_api.model.entites.Produit;
import nova.minisite_api.service.ServiceProduit;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@Tag(name = "Produits")
public class ProduitController {

    private final ServiceProduit serviceProduit;

    public ProduitController(ServiceProduit serviceProduit) {
        this.serviceProduit = serviceProduit;
    }

    // --- ACCÈS PUBLIC ---

    @Operation(summary = "Lister tous les produits")
    @GetMapping
    public ResponseEntity<List<Produit>> lister() {
        return ResponseEntity.ok(serviceProduit.lister());
    }

    @Operation(summary = "Détails d'un produit par ID")
    @GetMapping("/{id}")
    public ResponseEntity<Produit> details(@PathVariable Long id) {
        return ResponseEntity.ok(serviceProduit.trouverParId(id));
    }

    // --- ACCÈS ADMIN UNIQUEMENT ---

    @Operation(summary = "Créer un produit (Admin)")
    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produit> creer(
            @RequestPart("produit") ProduitDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(serviceProduit.creer(dto, image));
    }

    @Operation(summary = "Modifier un produit (Admin)")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Produit> modifier(
            @PathVariable Long id,
            @RequestPart("produit") ProduitDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(serviceProduit.modifier(id, dto, image));
    }

    @Operation(summary = "Supprimer un produit (Admin)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> supprimer(@PathVariable Long id) {
        serviceProduit.supprimer(id);
        return ResponseEntity.ok("Produit supprimé avec succès");
    }
}