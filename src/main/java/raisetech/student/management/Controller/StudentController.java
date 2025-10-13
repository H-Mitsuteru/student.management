package raisetech.student.management.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.Controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  private StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter= converter;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() { // 個別情報取得
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

//    List<StudentDetail> studentDetails = new ArrayList<>();
//        for (Student student : students) {
//          StudentDetail studentDetail = new StudentDetail();
//          studentDetail.setStudent(student);
    // 上の処理　ForEachを含むループを折りたたむと下になる
    return converter.convertStudentDetails(students, studentsCourses);
  }



  @GetMapping("/studentCourseList")
  public List<StudentsCourses> getStudentCourseList() {
    return service.searchStudentsCourseList();
  }
}
