package nova.mini_site.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nova.mini_site.model.dto.ProduitDto;
import nova.mini_site.model.entites.Produit;
import nova.mini_site.service.ServiceProduit;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/produits")
@Tag(name = "Produits (Admin)")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProduitController {

    private final ServiceProduit serviceProduit;

    public AdminProduitController(ServiceProduit serviceProduit) {
        this.serviceProduit = serviceProduit;
    }

    @Operation(summary = "Créer un produit")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Produit> creer(
            @RequestPart("produit") ProduitDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        return ResponseEntity.ok(serviceProduit.creer(dto, image));
    }

    @Operation(summary = "Modifier un produit")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Produit> modifier(
            @PathVariable Long id,
            @RequestPart("produit") ProduitDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        return ResponseEntity.ok(serviceProduit.modifier(id, dto, image));
    }

    @Operation(summary = "Supprimer un produit")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimer(@PathVariable Long id) {
        serviceProduit.supprimer(id); // Maintenant cette méthode existe !
        return ResponseEntity.ok("Produit supprimé avec succès");
    }
}