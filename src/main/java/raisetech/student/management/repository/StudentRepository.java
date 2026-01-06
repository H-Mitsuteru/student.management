package raisetech.student.management.repository;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.DataTransferObject.StudentSearchCondition;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルを紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧(全件)
   */
  // @Select("SELECT * FROM student") -> studentRepository.xmlへ移行
  List<Student> search(); // 個別情報取得

  /**
   * 受講生の検索を行います。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  Student searchStudent(String id);


  /**
   * 受講生の条件検索を行います。 受講生情報、受講生コース情報から条件に一致する受講生情報を取得します。
   *
   * @param condition　受講生検索条件（検索項目・AND/OR モードを含む）
   * @return　検索条件に一致した受講生情報の一覧
   */
  List<Student> searchByCondition(StudentSearchCondition condition);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報(全件)
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentID　受講生ID
   * @return　受講生IDに紐づく受講生コース情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentID}")
  List<StudentCourse> searchStudentCourse(String studentID);



  // 受講生登録
  //            #{studentID}, は自動生成なので省略
  // students の後の項目名を省略すると全項目が対象となる
  /**
   * 受講生新規登録します。 IDに関しては自動採番を行う。
   *
   * @param student　受講生
   */
  @Insert("INSERT INTO students (name, furigana, nickname, e_mail, live_municipality, age, gender, remark, is_deleted) "
      + "VALUES(#{name}, #{furigana}, #{nickname}, #{email}, #{liveMunicipality}, #{age}, #{gender}, #{remark}, false) ")
  @Options(useGeneratedKeys = true, keyProperty = "studentID")
  void registerStudent(Student student);


  // 受講生登録
  /**
   * 受講生コース情報を新規登録します。 IDに関しては自動採番を行う。
   * @param studentCourse　受講生コース情報
   */
  @Insert("""
        INSERT INTO students_Courses (
            status,
            student_id,
            course_name,
            start_date,
            end_date
        )
        VALUES(
            #{status},
            #{studentID},
            #{courseName},
            #{startDate},
            #{endDate}
        )
    """)
  @Options(useGeneratedKeys = true, keyProperty = "courseID")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   *
   * @param student　受講生
   */
  @Update("UPDATE students SET name = #{name}, furigana = #{furigana}, nickname = #{nickname}, "
    + "e_mail = #{email}, live_municipality = #{liveMunicipality}, age = #{age}, gender = #{gender}, "
    + "remark = #{remark}, is_deleted = #{isDeleted} WHERE student_id = #{studentID}")
void updateStudent(Student student);


  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourse　受講生コース情報
   */
  @Update("UPDATE students_Courses SET status = #{status}, course_name = #{courseName}, "
  + "start_date = #{startDate}, end_date = #{endDate} WHERE course_id = #{courseID}")
void updateStudentCourse(StudentCourse studentCourse);

}
