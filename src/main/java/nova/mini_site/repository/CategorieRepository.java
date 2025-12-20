package nova.mini_site.repository;

import nova.mini_site.model.entites.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    boolean existsByNom(String nom);
}
