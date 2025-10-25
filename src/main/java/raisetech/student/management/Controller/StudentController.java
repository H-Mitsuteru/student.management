package raisetech.student.management.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.lang.model.element.ModuleElement;
import javax.naming.Binding;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.Controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  private StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter= converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) { // 個別情報取得
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public String getStudentCourseList(Model model) {
    // 学生とコース情報を取得
    List<Student> students = service.searchStudentList();
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();

    // 学生ごとにコースをまとめる
    List<StudentDetail> studentDetails = converter.convertStudentDetails(students, studentsCourses);

    // HTMLに渡すデータ名は studentList（HTML側と一致）
    model.addAttribute("studentList", studentDetails);

    // 表示するHTML名（拡張子は不要）
    return "studentCourseList";
  }

//  @GetMapping("/studentCourseList")
//  public List<StudentsCourses> getStudentCourseList() {
//    return service.searchStudentsCourseList();
//  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail()); // 中身空っぽのStudentDetailを突っ込んでおく
    return "registerStudent";
  }

  @GetMapping("/newStudentCourse")
  public String newStudentCourse(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudentCourse";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if(result.hasErrors()) {  // エラーがあったら元の画面に戻す
      return "resisterStudent";
    }
    // 新規受講生情報を登録する処理を実装する
    service.insertStudent(studentDetail.getStudent());

    // コース情報も一緒に登録できる様に実装する。コースは単体で良い。
  return "redirect:/studentList"; // 受講一覧を実行
  }

  @PostMapping("/registerStudentCourse")
  public String registerStudentCourse(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if(result.hasErrors()) {
      return "resisterStudentCourse";
    }
//    service.insertStudentCourse((StudentsCourses) studentDetail.getStudentsCourses());
//    return "redirect:/studentCourseList";
    service.insertStudentCourse(studentDetail.getStudentCourse());
    return "redirect:/studentCourseList";
  }
}
