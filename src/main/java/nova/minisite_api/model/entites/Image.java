package nova.minisite_api.model.entites;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nom du fichier stocké
    @Column(nullable = false)
    private String nomFichier;

    // chemin ou URL
    @Column(nullable = false)
    private String chemin;

    @OneToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;
}

