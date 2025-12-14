package raisetech.student.management.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.TestException;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。 　全件検索を行うので、条件指定はしません。
   *
   * @return　受講生詳細一覧(全件)
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
//  public List<StudentDetail> getStudentList() throws TestException { // 個別情報取得
//    throw new TestException("エラーが発生しました。");
    public List<StudentDetail> getStudentList()  { // 個別情報取得
      return  service.searchStudentList();
  }

  /**
   * 受講生一覧の検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param studentID 受講生ID
   * @return　受講生
   */
  @Operation(summary = "受講生ID情報", description = "受講生のIDに紐づく情報を表示します。")
  @GetMapping("/student/{studentID}")
  public StudentDetail getStudent(
      @PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String studentID) {
    return service.searchStudent(studentID);
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourseList(Arrays.asList(new StudentCourse()));
    model.addAttribute("studentDetail", studentDetail); // 中身空っぽのStudentDetailを突っ込んでおく
    return "registerStudent";
  }

  @GetMapping("/students/search")
  public List<StudentDetail> search(@RequestParam Map<String, String> params){
    return service.search(params);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return　実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
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
  @Operation(summary = "情報更新", description = "受講生の詳細情報を更新します。")
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  @Operation(summary = "テストエラー", description = "例外処理発生時のエラー表示をテストします。")
  @GetMapping("/errorTest")
  public String throwErrorTest() {
    throw new RuntimeException("テストエラー：RuntimeException を発生させました！");
  }

  @GetMapping("/exception")
  public ResponseEntity<String> throwException() throws NotFoundException {
    throw new NotFoundException("このAPIは現在利用できません。古いURLとなっています。");
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

}

//  @ExceptionHandler(TestException.class)
//  public ResponseEntity<String> handleTestException(TestException ex) {
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
//  }
//}

//  @GetMapping("/studentsCourseList")
//  public List<StudentsCourses> getStudentsCourseList() {
//    return service.searchStudentsCourseList();
//  }

//  @GetMapping("/newStudentCourse")
//  public String newStudentCourse(Model model) {
//    model.addAttribute("studentDetail", new StudentDetail());
//    return "registerStudentCourse";
//  }