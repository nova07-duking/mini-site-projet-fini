package nova.minisite_api.repository;

import nova.minisite_api.model.entites.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    boolean existsByNom(String nom);
}
