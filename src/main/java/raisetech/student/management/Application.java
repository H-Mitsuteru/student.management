package raisetech.student.management;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Spring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class Application {

  private final Map<String, String> studentMap = new LinkedHashMap<>();

  @Autowired
  private StudentRepository repository;

  public static void main(String[] args) {
    // localhost:8080
    SpringApplication.run(Application.class, args);
  }

  @GetMapping("/studentsInfo")
  public Map<String, String> getStudentMap() { // Map形式にして項目名[name,age]を表示なしで年齢に"歳"追加
    Map<String, String> result = new HashMap<>();
    for (Student student: repository.findAll()) {
      result.put(student.getName(),student.getAge() + "歳"); // +歳にするにはMap内形式を全てStringにする
    }
    return result;
  }

  @GetMapping("/students")
  public List<Student> getStudents() { // MySQL項目に沿ったList表示
    return repository.findAll();
  }

  @GetMapping("/student")
  public String getStudent(@RequestParam String name) { // 個別情報取得
    Student student = repository.searchByName(name);
    return student.getName() + " " + student.getAge() + "歳";
  }

  @PostMapping("/student")
  public void registerStudent(String name, int age) { // 情報追加
    repository.registerStudent(name, age);
  }

  @PatchMapping("/student")
  public void updateStudentName(String name, int age) { // 年齢情報更新
    repository.updatestudent(name, age);
  }

  @DeleteMapping("/student")
  public void deleteStudent(String name) { // 個別情報削除
    repository.deleteStudent(name);
  }
}
