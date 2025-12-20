package nova.minisite_api.service;

import lombok.RequiredArgsConstructor;
import nova.minisite_api.exceptions.ConflitException;
import nova.minisite_api.exceptions.RessourceNonTrouveeException;
import nova.minisite_api.model.dto.CategorieDto;
import nova.minisite_api.model.entites.Categorie;
import nova.minisite_api.repository.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCategorie {

    private final CategorieRepository repository;

    public Categorie creer(CategorieDto dto) {
        if (repository.existsByNom(dto.getNom())) {
            throw new ConflitException("Cette catégorie existe déjà");
        }

        Categorie categorie = new Categorie();
        categorie.setNom(dto.getNom());
        categorie.setDescription(dto.getDescription());

        return repository.save(categorie);
    }

    public Categorie modifier(Long id, CategorieDto dto) {
        Categorie existante = repository.findById(id)
                .orElseThrow(() -> new RessourceNonTrouveeException("Catégorie introuvable"));

        existante.setNom(dto.getNom());
        existante.setDescription(dto.getDescription());

        return repository.save(existante);
    }

    public void supprimer(Long id) {
        if (!repository.existsById(id)) {
            throw new RessourceNonTrouveeException("Catégorie introuvable");
        }
        repository.deleteById(id);
    }

    public List<Categorie> lister() {
        return repository.findAll();
    }
}
