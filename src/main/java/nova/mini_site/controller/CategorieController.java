package nova.mini_site.controller;

import io.swagger.v3.oas.annotations.Operation;
import nova.mini_site.model.entites.Categorie;
import nova.mini_site.service.ServiceCategorie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final ServiceCategorie serviceCategorie;

    public CategorieController(ServiceCategorie serviceCategorie) {
        this.serviceCategorie = serviceCategorie;
    }

    @Operation(summary = "lister les categorie")
    @GetMapping
    public ResponseEntity<List<Categorie>> lister() {
        return ResponseEntity.ok(serviceCategorie.lister());
    }
}
