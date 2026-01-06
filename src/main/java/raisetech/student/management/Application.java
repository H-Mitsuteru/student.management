package raisetech.student.management;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "受講生管理システム"))
@SpringBootApplication
@MapperScan("raisetech.student.management.repository")

public class Application {

//  private final Map<String, String> studentMap = new LinkedHashMap<>();

  public static void main(String[] args) {
    // localhost:8080
    SpringApplication.run(Application.class, args);
  }
}
