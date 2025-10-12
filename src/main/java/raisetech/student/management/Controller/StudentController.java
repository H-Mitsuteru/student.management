package raisetech.student.management.Controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;

  private StudentController(StudentService service) {
    this.service = service;
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() { // 個別情報取得
    // リクエストの加工処理、入力チェックとか
    return service.searchStudentList();
  }

  @GetMapping("/studentCourseList")
  public List<StudentsCourses> getStudentCourseList() {
    return service.searchStudentsCourseList();
  }
}
