package coursera;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourseraApplication {
	public static void main(String[] args) {
		SpringApplication.run(CourseraApplication.class, args);
	}
}

