package nova.minisite_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // Ajoute cet import

@SpringBootApplication
// Cette ligne dit à Spring : "Regarde précisément ici pour trouver les @Entity"
@EntityScan(basePackages = "nova.minisite_api.model.entites")
public class MinisiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinisiteApplication.class, args);
	}
}