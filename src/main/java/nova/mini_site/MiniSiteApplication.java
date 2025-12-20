package nova.mini_site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // Ajoute cet import

@SpringBootApplication
// Cette ligne dit à Spring : "Regarde précisément ici pour trouver les @Entity"
@EntityScan(basePackages = "nova.mini_site.model.entites")
public class MiniSiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniSiteApplication.class, args);
	}
}