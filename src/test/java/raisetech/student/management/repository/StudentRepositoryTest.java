package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  // search
  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(7);
  }

  // searchStudent
  @Test
  void 受講生のidに紐づく受講生情報の検索が行えること() {
    Student actual= sut.searchStudent("1");

    assertThat(actual).isNotNull();
    assertThat(actual.getStudentID()).isEqualTo("1");
    assertThat(actual.getName()).isEqualTo("林 弥津輝");
    assertThat(actual.getFurigana()).isEqualTo("ハヤシ ミツテル");
    assertThat(actual.getNickname()).isEqualTo("ミツ");
    assertThat(actual.getEmail()).isEqualTo("mitsuteru@example.com");
    assertThat(actual.getLiveMunicipality()).isEqualTo("岐阜市水海道");
    assertThat(actual.getAge()).isEqualTo(52);
    assertThat(actual.getGender()).isEqualTo("男");
  }

  // searchStudentCourseList
  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(11);
  }

  // searchStudentCourse
  @Test
  void 受講生idに紐づく受講生コース情報の検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourse("1");
//    assertThat(actual).isNotNull();
//    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).hasSize(2);

    assertThat(actual.get(0).getCourseID()).isEqualTo("1");
    assertThat(actual.get(0).getCourseName()).isEqualTo("Java入門");
    assertThat(actual.get(1).getCourseID()).isEqualTo("2");
    assertThat(actual.get(1).getCourseName()).isEqualTo("Spring Boot実践");

    assertThat(actual)
        .extracting(StudentCourse::getStudentID)
        .containsOnly("1");
  }

  // registerStudent
  @Test
  void 受講生の新規登録時に自動採番されたIDが設定されること() {
    // 準備：登録する Student オブジェクト
    Student student = new Student();
    student.setName("テスト太郎");
    student.setFurigana("テスト タロウ");
    student.setNickname("タロちゃん");
    student.setEmail("taro@example.com");
    student.setLiveMunicipality("東京都港区");
    student.setAge(30);
    student.setGender("男");
    student.setRemark("自動採番テスト");

    // 実行
    sut.registerStudent(student);

    // 検証：自動採番された ID が student オブジェクトへセットされていること
    assertThat(student.getStudentID()).isNotNull();

    // 既存 data.sql では student_id は 1〜7
    // → 次の採番値は 8 であるはず
    assertThat(student.getStudentID()).isEqualTo("8");

    // データベースに登録されたことを確認
    Student actual = sut.searchStudent(student.getStudentID());
    assertThat(actual.getName()).isEqualTo("テスト太郎");
    assertThat(actual.getEmail()).isEqualTo("taro@example.com");
  }

  // registerStudentCourse
  @Test
  void 受講生コースの登録が行えること() {
    // 日付を変数で固定
    LocalDateTime start = LocalDateTime.now().withNano(0);
    LocalDateTime end = start.plusYears(1);

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentID("8");
    studentCourse.setCourseName("Java_Test");
    studentCourse.setStartDate(start);
    studentCourse.setEndDate(end);

    sut.registerStudentCourse(studentCourse);

    assertThat(studentCourse.getCourseID()).isNotNull();
    assertThat(studentCourse.getCourseID()).isEqualTo("12");

    List<StudentCourse> actual = sut.searchStudentCourse(studentCourse.getStudentID());
    assertThat(actual.get(0).getCourseID()).isEqualTo("12");
    assertThat(actual.get(0).getStudentID()).isEqualTo("8");
    assertThat(actual.get(0).getCourseName()).isEqualTo("Java_Test");
    assertThat(actual.get(0).getStartDate()).isEqualTo(start);
    assertThat(actual.get(0).getEndDate()).isEqualTo(end);
  }

  // updateStudent
  @Test
  void 受講生の更新が行えること() {
    // updateするオブジェクト作成
    Student student = new Student();
    student.setStudentID("1");
    student.setName("更新 太郎");
    student.setFurigana("コウシン タロウ");
    student.setAge(99);
    student.setEmail("update@example.com");

    sut.updateStudent(student);

    // 再取得
    Student actual = sut.searchStudent("1");

    assertThat(actual.getStudentID()).isEqualTo("1");
    assertThat(actual.getName()).isEqualTo("更新 太郎");
    assertThat(actual.getFurigana()).isEqualTo("コウシン タロウ");
    assertThat(actual.getAge()).isEqualTo(99);
    assertThat(actual.getEmail()).isEqualTo("update@example.com");
  }

  // updateStudentCourse
  @Test
  void 受講生コースの更新が成功すること() {
    // ---- given ----
    var before = sut.searchStudentCourse("1"); //← course_id=1 のコース取得
    assertThat(before.get(0).getCourseName()).isEqualTo("Java入門");

    StudentCourse update = new StudentCourse();
    update.setCourseID("1");
    update.setCourseName("Java超入門");
    update.setStartDate(before.get(0).getStartDate());
    update.setEndDate(before.get(0).getEndDate());

    // ---- when ----
    sut.updateStudentCourse(update);

    // ---- then ----
    var after = sut.searchStudentCourse("1");
    assertThat(after.get(0).getCourseName()).isEqualTo("Java超入門");

    System.out.println("【コース名変更前】" + before.get(0).getCourseName());
    System.out.println("【コース名変更後】" + after.get(0).getCourseName());
  }

}