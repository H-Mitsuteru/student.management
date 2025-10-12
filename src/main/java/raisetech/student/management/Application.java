package raisetech.student.management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.repository.StudentRepository;

@SpringBootApplication
public class Application {

//  private final Map<String, String> studentMap = new LinkedHashMap<>();

  public static void main(String[] args) {
    // localhost:8080
    SpringApplication.run(Application.class, args);
  }
}
