package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.Controller.converter.StudentConverter;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter= converter;
  }

  /**
   * 受講生詳細の一覧検索を行います。
   * 全件検索を行うので、条件指定は行いません。
   * @return　受講生詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   * @param id　受講生ID
   * @return　受講生詳細
   */
  public StudentDetail searchStudent(String id) {
    Student student = repository.searchStudent(id);
    List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getStudentID());
    return new StudentDetail(student, studentCourse);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します。
   *
   * @param studentDetail　受講生情報
   * @return　登録情報を付与した受講生詳細
   */
  @Transactional // 登録、更新した時のエラーをロールバックしてくれる
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    // 受講生登録
    Student student = studentDetail.getStudent();
    repository.registerStudent(student);

    // コース登録
    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      // status 初期値 (仮申込み)
      studentCourse.setStatus(CourseStatus.仮申込み);
      // start、end、studentID 自動セット
      initStudentsCourse(studentCourse, student.getStudentID());

      repository.registerStudentCourse(studentCourse);
    });
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse　受講生コース情報
   * @param studentID　受講生
   */
 void initStudentsCourse(StudentCourse studentCourse, String studentID) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentID(studentID);
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。　受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail　受講生詳細
   */
  @Transactional // 登録、更新した時のエラーをロールバックしてくれる
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    //      studentCourse.setStudentID(studentDetail.getStudent().getStudentID());
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> {
          validateCourseStatus(studentCourse);
          repository.updateStudentCourse(studentCourse);
        });
  }

//  private void validateCourseDate(StudentCourse course) {
//    // 受講終了なら endDate 必須
//    if (course.getStatus() == CourseStatus.受講終了 && course.getEndDate() == null) {
//      throw new IllegalArgumentException("受講終了の場合はendDateの日付を入力してください。");
//    }
//
//    if (course.getStartDate() != null && course.getEndDate() != null) {
//      if (!course.getStartDate().isBefore(course.getEndDate())) {
//        throw new IllegalArgumentException("開始日が終了日より前である必要があります。");
//      }
//    }
//  }

  private void validateCourseStatus(StudentCourse course) {

    if (course.getStatus() == null) {
      throw new IllegalArgumentException("status が指定されていません。");
    }

    // Enum に存在しない値は弾く（必須チェック＋申込みリストチェック）
    boolean exists = Arrays.stream(CourseStatus.values())
        .anyMatch(s -> s == course.getStatus());

    if (!exists) {
      throw new IllegalArgumentException("指定された status は無効です。使用可能な値: "
          + Arrays.toString(CourseStatus.values()));
    }
  }

//  private void validateRequired(StudentCourse course) {
//
//    CourseStatus status = course.getStatus();
//
//    List<String> missing = new ArrayList<>();
//
//    if (status.getRequiredFields().contains("courseName") && course.getCourseName() == null) {
//      missing.add("コース名");
//    }
//
//    if (status.getRequiredFields().contains("startDate") && course.getStartDate() == null) {
//      missing.add("開始日");
//    }
//
//    if (status.getRequiredFields().contains("endDate") && course.getEndDate() == null) {
//      missing.add("終了日");
//    }
//
//    if (!missing.isEmpty()) {
//      throw new IllegalArgumentException(
//          status.name() + " では以下が必須です: " + String.join(", ", missing)
//      );
//    }
//  }

}
