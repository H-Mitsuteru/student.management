package raisetech.student.management.Controller.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生リストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseID("10");
    studentCourse.setStudentID("1");
    studentCourse.setCourseName("Java");
    studentCourse.setStartDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentCourseList);
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること() {
    Student student = createStudent();

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseID("10");
    studentCourse.setStudentID("2");
    studentCourse.setCourseName("Java");
    studentCourse.setStartDate(LocalDateTime.now());
    studentCourse.setEndDate(LocalDateTime.now().plusYears(1));

    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    assertThat(actual.get(0).getStudent()).isEqualTo(student);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();
  }

  private static Student createStudent() {
    Student student = new Student();
    student.setStudentID("1");
    student.setName("江波公史");
    student.setFurigana("エナミコウジ");
    student.setNickname("コウジ");
    student.setEmail("test@example.com");
    student.setGender("男");
    student.setLiveMunicipality("奈良県");
    student.setAge(36);
    student.setRemark("");
    student.setDeleted(false);
    return student;
  }


}

//  private StudentConverter converter;
//
//  private Student s1;
//  private Student s2;
//
//  private StudentCourse sc1;
//  private StudentCourse sc2;
//  private StudentCourse sc3;
//
//  @BeforeEach
//  void setUp() {
//    converter = new StudentConverter();
//
//    // --- Student ---
//    s1 = new Student();
//    s1.setStudentID("1");
//    s1.setName("太郎");
//
//    s2 = new Student();
//    s2.setStudentID("2");
//    s2.setName("花子");
//
//    // --- StudentCourse ---
//    sc1 = new StudentCourse();
//    sc1.setStudentID("1");
//    sc1.setCourseID("10");
//
//    sc2 = new StudentCourse();
//    sc2.setStudentID("1");
//    sc2.setCourseID("11");
//
//    sc3 = new StudentCourse();
//    sc3.setStudentID("2");
//    sc3.setCourseID("20");
//  }
//
//  @Test
//  void convertStudentDetails_正常系_StudentにCourseが紐づく() {
//
//    List<Student> studentList = List.of(s1, s2);
//    List<StudentCourse> courseList = List.of(sc1, sc2, sc3);
//
//    List<StudentDetail> result = converter.convertStudentDetails(studentList, courseList);
//
//    assertEquals(2, result.size());
//
//    // --- s1 の検証 ---
//    StudentDetail d1 = result.get(0);
//    assertEquals("1", d1.getStudent().getStudentID());
//    assertEquals(2, d1.getStudentCourseList().size());
//
//    // --- s2 の検証 ---
//    StudentDetail d2 = result.get(1);
//    assertEquals("2", d2.getStudent().getStudentID());
//    assertEquals(1, d2.getStudentCourseList().size());
//    assertEquals("20", d2.getStudentCourseList().get(0).getCourseID());
//  }
//
//  @Test
//  void convertStudentDetails_StudentIDがnullの場合_Courseが紐付かない() {
//
//    Student sNull = new Student();
//    sNull.setStudentID(null);
//    sNull.setName("Unknown");
//
//    List<Student> studentList = List.of(sNull);
//    List<StudentCourse> courseList = List.of(sc1); // sc1 は studentId=1
//
//    List<StudentDetail> result = converter.convertStudentDetails(studentList, courseList);
//
//    assertEquals(1, result.size());
//    assertTrue(result.get(0).getStudentCourseList().isEmpty()); // 紐付かない
//  }
//
//  @Test
//  void convertStudentDetails_Courseリストが空でも動作する() {
//
//    List<Student> studentList = List.of(s1);
//    List<StudentCourse> courseList = new ArrayList<>();
//
//    List<StudentDetail> result = converter.convertStudentDetails(studentList, courseList);
//
//    assertEquals(1, result.size());
//    assertTrue(result.get(0).getStudentCourseList().isEmpty());
//  }
//}