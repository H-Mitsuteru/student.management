package raisetech.student.management.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.DataTransferObject.StudentSearchCondition;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  @MockBean
  StudentRepository repository;


  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
//    when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));
    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());
//        .andExpect(content().json("[{\"student\":null,\"studentCourseList\":null}]"));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の検索が実行できて空で返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/student/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 仮申込みで検索が出来て該当する情報が返ってくること() throws Exception {

    // given
    Student student = new Student();
    student.setStudentID("1");
    student.setName("山田 太郎");

    StudentCourse course = new StudentCourse();
    course.setStatus(CourseStatus.仮申込み);

    StudentDetail detail =
        new StudentDetail(student, List.of(course));

    when(service.search(any(StudentSearchCondition.class)))
        .thenReturn(List.of(detail));

    // when & then
    mockMvc.perform(get("/students/search")
            .param("status", "仮申込み"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].student.studentID").value("1"))
        .andExpect(jsonPath("$[0].student.name").value("山田 太郎"))
        .andExpect(jsonPath("$[0].studentCourseList[0].status").value("仮申込み"));

    verify(service).search(any(StudentSearchCondition.class));
  }

  @Test
  void 氏名とコース名を指定した場合に検索できること() throws Exception {

    // given
    Student student = new Student();
    student.setStudentID("2");
    student.setName("山田 修");

    StudentDetail detail =
        new StudentDetail(student, List.of());

    when(service.search(any(StudentSearchCondition.class)))
        .thenReturn(List.of(detail));

    // when & then
    mockMvc.perform(get("/students/search")
            .param("name", "山田")
            .param("courseName", "Java"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].student.name").value("山田 修"));
  }

  @Test
  void OR検索モードで検索できること() throws Exception {

    // given
    when(service.search(any(StudentSearchCondition.class)))
        .thenReturn(List.of());

    // when & then
    mockMvc.perform(get("/students/search")
            .param("name", "山田")
            .param("email", "example.com")
            .param("mode", "OR"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void 検索条件なしでもエラーにならないこと() throws Exception {

    // given
    when(service.search(any(StudentSearchCondition.class)))
        .thenReturn(List.of());

    // when & then
    mockMvc.perform(get("/students/search"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }


  @Test
  void 受講生詳細の登録が実行できて空で返ってくること() throws Exception {
    // リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    // 本来であれば返りは登録されたデータが入るが、モック化すると意味がない為、レスポンスは作らない。
    mockMvc.perform(post("/registerStudent").contentType(MediaType.APPLICATION_JSON).content(
        """
        {
            "student": {
                "name": "テスト太郎",
                "furigana": "テストタロウ",
                "email": "test@example.com",
                "liveMunicipality": "東京",
                "gender": "その他"
            },
            "studentCourseList": [
                {
                    "courseName": "Java"
                }
           ]
        }
        """
    ))
    .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any());
}

  @Test
  void 受講生詳細の更新が実行できて空で返ってくること() throws Exception {
    // リクエストデータは適切に構築して入力チェックの検証も兼ねている。
    mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
                "student": {
                    "studentID": "12",
                    "name": "テスト太郎",
                    "furigana": "テストタロウ",
                    "email": "test@example.com",
                    "liveMunicipality": "東京",
                    "gender": "男",
                    "remark": ""
                },
                "studentCourseList": [
                    {
                        "courseID": "15",
                        "studentID": "12",
                        "courseName": "Java",
                        "startDate": "2024-01-01T00:00:00",
                        "endDate": "2025-01-01T00:00:00"
                    }
               ]
            }
            """
        ))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any());
  }

@Test
void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
    mockMvc.perform(get("/exception"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
}


// ChatGPTにHelpして
// ================================
// 正常系：数字IDで StudentDetail を取得できる
// ================================
@Test
void 受講生IDで検索してStudentDetailが返ってくること() throws Exception {

// モック戻り値の準備
  Student student = new Student();
  student.setStudentID("10");
  student.setName("テスト太郎");

  StudentCourse course = new StudentCourse();
  course.setStudentID("10");

  StudentDetail mockDetail =
      new StudentDetail(student, List.of(course));

  when(service.searchStudent("10")).thenReturn(mockDetail);

// 実行
  mockMvc.perform(get("/student/10"))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.student.studentID").value("10"))
    .andExpect(jsonPath("$.student.name").value("テスト太郎"))
    .andExpect(jsonPath("$.studentCourseList[0].studentID").value("10"));

  verify(service, times(1)).searchStudent("10");
}

// ================================
// 異常系：数字以外（バリデーションエラー）
// ================================
@Test
void 数字以外のIDを指定するとバリデーションエラーで400になること() throws Exception {
  mockMvc.perform(get("/student/abc"))
    .andExpect(status().isInternalServerError())
    /* レスポンスメッセージの内容確認 */
    .andExpect(content().string(containsString("must match")));

  verify(service, never()).searchStudent(anyString());
}

// ================================
// 異常系：空文字（NotBlank + Pattern）
// ================================
@Test
void 空欄のIDを指定すると400になること() throws Exception {
  mockMvc.perform(get("/student/ "))
    .andExpect(status().isInternalServerError())
    /* レスポンスメッセージの内容確認 */
    .andExpect(content().string(containsString("must match")));

  verify(service, never()).searchStudent(anyString());
}

// ================================
// newStudent でstudentDetailをModelへ
// ================================
@Test
void newStudent画面が表示されて空のStudentDetailがModelに入っていること() throws Exception {
  mockMvc.perform(get("/newStudent"))
    .andExpect(status().isOk())
    .andExpect(content().string("registerStudent"));
//        .andExpect(model().attributeExists("studentDetail"));

  verifyNoInteractions(service);
}

@Test
void registerStudent_正常系_登録成功して200が返る() throws Exception {

  String requestJson = """
        {
          "student": {
            "studentID": "1",
            "name": "テスト太郎",
            "furigana": "テストタロウ",
            "email": "test@example.com",
            "liveMunicipality": "東京",
            "gender": "その他"
          },
          "studentCourseList": [
            {
              "courseID": "10",
              "studentID": "1",
              "courseName": "Java",
              "startDate": "2024-01-01T00:00:00",
              "endDate": "2025-01-01T00:00:00"
            }
          ]
        }
        """;

    // --- モックレスポンス ---
    Student student = new Student();
    student.setStudentID("1");
    student.setName("テスト太郎");
    student.setFurigana("テストタロウ");
    student.setEmail("test@example.com");
    student.setLiveMunicipality("東京");
    student.setGender("その他");

    StudentCourse course = new StudentCourse();
    course.setCourseID("10");
    course.setStudentID("1");
    course.setCourseName("Java");
    course.setStartDate(LocalDateTime.of(2024,1,1,0,0));
    course.setEndDate(LocalDateTime.of(2025,1,1,0,0));

    StudentDetail responseDetail = new StudentDetail();
    responseDetail.setStudent(student);
    responseDetail.setStudentCourseList(List.of(course));

    when(service.registerStudent(any(StudentDetail.class)))
        .thenReturn(responseDetail);

    // --- 実行 ---
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.studentID").value("1"))
        .andExpect(jsonPath("$.studentCourseList[0].courseID").value("10"));
  }

  @Test
  void updateStudent_正常系_更新成功して200が返る() throws Exception {

    String requestJson = """
      {
        "student": {
          "studentID": "1",
          "name": "更新太郎",
          "furigana": "コウシンタロウ",
          "email": "update@example.com",
          "liveMunicipality": "大阪",
          "gender": "その他"
        },
        "studentCourseList": [
          {
            "courseID": "10",
            "studentID": "1",
            "courseName": "Java上級",
            "startDate": "2024-01-01T00:00:00",
            "endDate": "2025-01-01T00:00:00"
          }
        ]
      }
      """;

    // ---- void メソッドのモック ----
    doNothing().when(service).updateStudent(any(StudentDetail.class));

    // ---- 実行 & 検証 ----
    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));
  }

  // 入力チェック
  @Test
  void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
    Student student = new Student();
    student.setStudentID("999");
    student.setName("江波公史");
    student.setFurigana("エナミコウジ");
    student.setEmail("test@example.com");
    student.setLiveMunicipality("奈良県");
    student.setGender("男");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0); /* NotBlank数 */
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に入力チェックに掛かること() {
    Student student = new Student();
    student.setStudentID("テストです");
    student.setName("江波公史");
    student.setFurigana("エナミコウジ");
    student.setEmail("test@example.com");
    student.setLiveMunicipality("奈良県");
    student.setGender("男");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message")
        .containsOnly("数字のみ入力するようにして下さい。");
  }
}