package nova.minisite_api.service;

import lombok.RequiredArgsConstructor;
import nova.minisite_api.model.dto.ProduitDto;
import nova.minisite_api.model.entites.Categorie;
import nova.minisite_api.model.entites.Produit;
import nova.minisite_api.repository.CategorieRepository;
import nova.minisite_api.repository.ProduitRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProduit {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;

    // Chemin où les images seront stockées
    private final String UPLOAD_DIR = "uploads/produits";

    public Produit trouverParId(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID : " + id));
    }

    @Transactional
    public Produit creer(ProduitDto dto, MultipartFile image) {
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        Produit produit = new Produit();
        return remplirEtSauvegarder(dto, image, produit, categorie);
    }

    @Transactional
    public Produit modifier(Long id, ProduitDto dto, MultipartFile image) {
        Produit existant = trouverParId(id);
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));

        return remplirEtSauvegarder(dto, image, existant, categorie);
    }

    private Produit remplirEtSauvegarder(ProduitDto dto, MultipartFile image, Produit produit, Categorie categorie) {
        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setPrix(dto.getPrix());
        produit.setCategorie(categorie);

        // LOGIQUE D'ENREGISTREMENT PHYSIQUE DE L'IMAGE
        if (image != null && !image.isEmpty()) {
            String nomFichierGénéré = enregistrerFichierPhysique(image);
            produit.setImageUrl(nomFichierGénéré);
        }

        return produitRepository.save(produit);
    }

    private String enregistrerFichierPhysique(MultipartFile file) {
        try {
            // 1. Créer le chemin du dossier
            Path uploadPath = Paths.get(UPLOAD_DIR);

            // 2. Créer le dossier s'il n'existe pas
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 3. Générer un nom unique pour éviter les doublons (ex: UUID_image.jpg)
            String extension = "";
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String nomFichier = UUID.randomUUID().toString() + extension;

            // 4. Copier le fichier dans le dossier
            Path cible = uploadPath.resolve(nomFichier);
            Files.copy(file.getInputStream(), cible, StandardCopyOption.REPLACE_EXISTING);

            return nomFichier; // On retourne le nom stocké en BDD
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage de l'image : " + e.getMessage());
        }
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