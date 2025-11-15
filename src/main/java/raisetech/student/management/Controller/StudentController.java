package raisetech.student.management.Controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  private StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。
   *　全件検索を行うので、条件指定はしません。
   *
   * @return　受講生詳細一覧(全件)
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() { // 個別情報取得
    return  service.searchStudentList();
  }

  /**
   * 受講生一覧の検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param studentID　受講生ID
   * @return　受講生
   */
  @GetMapping("/student/{studentID}")
  public StudentDetail getStudent(@PathVariable String studentID) {
    return service.searchStudent(studentID);
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourseList(Arrays.asList(new StudentCourse()));
    model.addAttribute("studentDetail", studentDetail); // 中身空っぽのStudentDetailを突っ込んでおく
    return "registerStudent";
  }

  /**
   * 受講生詳細の登録を行います。
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    // コース情報も一緒に登録できる様に実装する。コースは単体で良い。
  return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。 キャンセルフラグの更新もここで行います(論理削除)
   *
   * @param studentDetail 受講生詳細
   * @return　実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}

//  @GetMapping("/studentsCourseList")
//  public List<StudentsCourses> getStudentsCourseList() {
//    return service.searchStudentsCourseList();
//  }

//  @GetMapping("/newStudentCourse")
//  public String newStudentCourse(Model model) {
//    model.addAttribute("studentDetail", new StudentDetail());
//    return "registerStudentCourse";
//  }