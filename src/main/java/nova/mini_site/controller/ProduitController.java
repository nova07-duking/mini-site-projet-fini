package nova.mini_site.controller;

import io.swagger.v3.oas.annotations.Operation;
import nova.mini_site.model.entites.Produit;
import nova.mini_site.service.ServiceProduit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ServiceProduit serviceProduit;

    public ProduitController(ServiceProduit serviceProduit) {
        this.serviceProduit = serviceProduit;
    }

    @Operation(summary = "lister les produit")
    @GetMapping
    public ResponseEntity<List<Produit>> lister() {
        return ResponseEntity.ok(serviceProduit.lister());
    }

    @Operation(summary = "detail un produit avec id")
    @GetMapping("/{id}")
    public ResponseEntity<Produit> details(@PathVariable Long id) {
        return ResponseEntity.ok(serviceProduit.trouverParId(id));
    }
}
