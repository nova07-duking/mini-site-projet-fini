package nova.mini_site.service;

import lombok.RequiredArgsConstructor;
import nova.mini_site.model.dto.ProduitDto;
import nova.mini_site.model.entites.Categorie;
import nova.mini_site.model.entites.Produit;
import nova.mini_site.repository.CategorieRepository;
import nova.mini_site.repository.ProduitRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProduit {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;

    // Méthode pour récupérer un produit par son ID
    public Produit trouverParId(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID : " + id));
    }

    public Produit creer(ProduitDto dto, MultipartFile image) {
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        Produit produit = new Produit();
        return getProduit(dto, image, produit, categorie);
    }

    public Produit modifier(Long id, ProduitDto dto, MultipartFile image) {
        // On réutilise la méthode trouverParId ici pour éviter la duplication de code
        Produit existant = trouverParId(id);

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        return getProduit(dto, image, existant, categorie);
    }

    @NonNull
    private Produit getProduit(ProduitDto dto, MultipartFile image, Produit existant, Categorie categorie) {
        existant.setNom(dto.getNom());
        existant.setDescription(dto.getDescription());
        existant.setPrix(dto.getPrix());
        existant.setCategorie(categorie);

        if (image != null && !image.isEmpty()) {
            existant.setImageUrl(image.getOriginalFilename());
        }

        return produitRepository.save(existant);
    }

    public List<Produit> lister() {
        return produitRepository.findAll();
    }

    public void supprimer(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new RuntimeException("Impossible de supprimer : Produit introuvable");
        }
        produitRepository.deleteById(id);
    }
}