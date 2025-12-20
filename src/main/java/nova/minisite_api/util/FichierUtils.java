package nova.minisite_api.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

public class FichierUtils {

    private static final String DOSSIER_UPLOAD = "uploads/";

    public static String enregistrerImage(MultipartFile fichier) {

        if (fichier.isEmpty()) {
            throw new RuntimeException("Fichier vide");
        }

        try {
            Files.createDirectories(Paths.get(DOSSIER_UPLOAD));

            String nomFichier = UUID.randomUUID() + "_" + fichier.getOriginalFilename();
            Path chemin = Paths.get(DOSSIER_UPLOAD + nomFichier);

            Files.copy(fichier.getInputStream(), chemin, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + nomFichier;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload de l'image");
        }
    }
}
