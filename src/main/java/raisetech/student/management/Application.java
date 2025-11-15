package raisetech.student.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

//  private final Map<String, String> studentMap = new LinkedHashMap<>();

  public static void main(String[] args) {
    // localhost:8080
    SpringApplication.run(Application.class, args);
  }
}
