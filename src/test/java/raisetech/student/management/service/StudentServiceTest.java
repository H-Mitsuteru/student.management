package raisetech.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.Controller.converter.StudentConverter;
import raisetech.student.management.DataTransferObject.StudentSearchCondition;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    // Mockito Stub
    // 事前準備
    /* 上で宣言している */
    // StudentService sut = new StudentService(repository, converter);
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    /* Mockitoを省略：static メソッドのインポートを実行　*/
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    List<StudentDetail> actual = sut.searchStudentList();

    // 実行
    // List<StudentDetail> expected = new ArrayList<>();

    //検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList,studentCourseList);
    // Assertions.assertEquals(expected, actual);
  }

  @Test
  void 受講生詳細の検索_リポジトリの処理が適切に呼び出せていること() {
    String id = "999";
    Student student = new Student();
    student.setStudentID(id);
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(id)).thenReturn(new ArrayList<>());

    StudentDetail expected = new StudentDetail(student, new ArrayList<>());

    StudentDetail actual = sut.searchStudent(id);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(id);
    Assertions.assertEquals(expected.getStudent().getStudentID(), actual.getStudent().getStudentID());
  }

  @Test
  @DisplayName("status を指定した場合、受講生詳細が返却されること")
  void search_byCourseStatus() {

    // given
    StudentSearchCondition condition = new StudentSearchCondition();
    condition.setStatus(CourseStatus.仮申込み);

    Student student = new Student(); // createStudent("1", "山田 太郎");
      student.setStudentID("1");
      student.setName("山田 太郎");
    List<Student> students = List.of(student);

    StudentCourse course = new StudentCourse(); //createStudentCourse("1", CourseStatus.仮申込み);
      course.setStudentID("1");
      course.setStatus(CourseStatus.仮申込み); //.setStatus.仮申込み;
    List<StudentCourse> courses = List.of(course);

    StudentDetail detail = new StudentDetail(student, List.of(course));
    List<StudentDetail> expected = List.of(detail);

    when(repository.searchByCondition(condition)).thenReturn(students);
    when(repository.searchStudentCourseList()).thenReturn(courses);
    when(converter.convertStudentDetails(students, courses)).thenReturn(expected);

    // when
    List<StudentDetail> actual = sut.search(condition);

    // then
    assertThat(actual).isEqualTo(expected);

    verify(repository).searchByCondition(condition);
    verify(repository).searchStudentCourseList();
    verify(converter).convertStudentDetails(students, courses);
  }

  @Test
  void 受講生詳細の登録_リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細の登録_初期化処理が行われること() {
    String id = "999";
    Student student = new Student();
    student.setStudentID(id);
    StudentCourse studentCourse = new StudentCourse();

    sut.initStudentsCourse(studentCourse, student.getStudentID());

    assertEquals("999", studentCourse.getStudentID());
    assertEquals(LocalDateTime.now().getHour(), studentCourse.getStartDate().getHour());
    assertEquals(LocalDateTime.now().plusYears(1).getYear(), studentCourse.getEndDate().getYear());
  }

  @Test
  void 受講生詳細の更新_リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStatus(CourseStatus.仮申込み);

    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }

/* 以下は自分で作成した場合 */
  @Test
  /* リポジトリからsearchStudent(id)を呼び出して帰ってきたStudentIDを使ってリポジトリからコース情報を呼び出せること */
  void 受講生詳細検索_リポジトリが正しく呼び出せて期待通りの結果が返ること() {
    String id = "5";

    Student student = new Student();
    student.setStudentID("5");
    student.setName("山田太郎");

    StudentCourse sc1 = new StudentCourse();
    sc1.setStudentID("5");
    sc1.setCourseID("10");
    sc1.setCourseName("Javaコース");

    StudentCourse sc2 = new StudentCourse();
    sc2.setStudentID("5");
    sc2.setCourseID("15");
    sc2.setCourseName("AWSコース");

    List<StudentCourse> courseList = List.of(sc1, sc2);

    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse("5")).thenReturn(courseList);

    StudentDetail actual = sut.searchStudent(id);

    // ★ コンソール表示（ここを追加）
    System.out.println("【受講生情報】");
    System.out.println(actual.getStudent());

    System.out.println("【コース情報】");
    actual.getStudentCourseList().forEach(System.out::println);

    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse("5");

    assertEquals(student, actual.getStudent());
    assertEquals(courseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生登録_受講生とコースが正しく登録されること() {
    // Arrange
    Student student = new Student();
    student.setStudentID("10");
    student.setName("田中太郎");
    student.setFurigana("タナカタロウ");
    student.setEmail("tanaka@example.com");
    student.setLiveMunicipality("横浜");
    student.setGender("男");

    StudentCourse sc1 = new StudentCourse();
    sc1.setCourseID("100");
    sc1.setCourseName("Java基礎");

    StudentCourse sc2 = new StudentCourse();
    sc2.setCourseID("200");
    sc2.setCourseName("Spring入門");

    List<StudentCourse> courseList = List.of(sc1, sc2);
    StudentDetail detail = new StudentDetail(student, courseList);

    // Act
    StudentDetail actual = sut.registerStudent(detail);

    // Assert
    // 受講生登録
    verify(repository).registerStudent(student);

    // コース登録が 2 回
    verify(repository, times(2)).registerStudentCourse(any(StudentCourse.class));

    // initStudentsCourse の効果確認
    assertEquals("10", sc1.getStudentID());
    assertEquals("10", sc2.getStudentID());
    assertNotNull(sc1.getStartDate());
    assertNotNull(sc1.getEndDate());
    assertNotNull(sc2.getStartDate());
    assertNotNull(sc2.getEndDate());

    assertEquals(detail, actual);
  }

  @Test
  void 受講生更新_受講生とコースが正しく更新されること() {
    // Arrange
    Student student = new Student();
    student.setStudentID("20");
    student.setName("佐藤次郎");
    student.setFurigana("サトウジロウ");
    student.setEmail("sato@example.com");
    student.setLiveMunicipality("大阪");
    student.setGender("男");

    StudentCourse sc1 = new StudentCourse();
    sc1.setCourseID("101");
    sc1.setStatus(CourseStatus.仮申込み);
    sc1.setStudentID("20");
    sc1.setCourseName("Java");

    StudentCourse sc2 = new StudentCourse();
    sc2.setCourseID("102");
    sc2.setStatus(CourseStatus.本申込み);
    sc2.setStudentID("20");
    sc2.setCourseName("Spring");

    List<StudentCourse> courseList = List.of(sc1, sc2);

    StudentDetail detail = new StudentDetail(student, courseList);

    // Act
    sut.updateStudent(detail);

    // Assert
    verify(repository).updateStudent(student);
    verify(repository, times(2)).updateStudentCourse(any(StudentCourse.class));
  }

  @Test
  void コース登録時に開始日と終了日が設定されること() {
    // Arrange
    Student student = new Student();
    student.setStudentID("30");
    student.setName("中村一郎");
    student.setFurigana("ナカムライチロウ");
    student.setEmail("nakamura@example.com");
    student.setLiveMunicipality("札幌");
    student.setGender("男");

    StudentCourse sc = new StudentCourse();
    sc.setCourseID("555");
    sc.setCourseName("Java初級");

    StudentDetail detail = new StudentDetail(student, List.of(sc));

    LocalDateTime before = LocalDateTime.now();

    // Act
    sut.registerStudent(detail);

    LocalDateTime after = LocalDateTime.now();

    // Assert
    assertEquals("30", sc.getStudentID());

    assertTrue(!sc.getStartDate().isBefore(before));
    assertTrue(!sc.getStartDate().isAfter(after));

    assertEquals(1, java.time.Period.between(
        sc.getStartDate().toLocalDate(),
        sc.getEndDate().toLocalDate()
    ).getYears());
  }

}