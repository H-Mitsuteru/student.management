package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ。
 *
 * 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */

@Mapper
public interface StudentRepository {

  /**
   * 全件検索します。
   * <p>
   * 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> search(); // 個別情報取得

  @Select("SELECT * FROM students WHERE student_id = #{studentID}")
  Student searchStudent(String id);

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCoursesList();

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentID}")
  List<StudentsCourses> searchStudentsCourses(String studentID);

  // 受講生登録
//            #{studentID}, は自動生成なので省略
  // students の後の項目名を省略すると全項目が対象となる
  @Insert("INSERT INTO students (name, furigana, nickname, e_mail, live_municipality, age, gender, remark, is_deleted) "
      + "VALUES(#{name}, #{furigana}, #{nickname}, #{email}, #{liveMunicipality}, #{age}, #{gender}, #{remark}, false) ")
  @Options(useGeneratedKeys = true, keyProperty = "studentID")
  void registerStudent(Student student);

  // 受講生登録
  @Insert("""
        INSERT INTO students_Courses (
            student_id,
            course_name,
            start_date,
            end_date
        )
        VALUES(
            #{studentID},
            #{courseName},
            #{startDate},
            #{endDate}
        )
    """)
  @Options(useGeneratedKeys = true, keyProperty = "studentID")
  void registerStudentsCourses(StudentsCourses studentsCourses);


@Update("UPDATE students SET name = #{name}, furigana = #{furigana}, nickname = #{nickname}, "
    + "e_mail = #{email}, live_municipality = #{liveMunicipality}, age = #{age}, gender = #{gender}, "
    + "remark = #{remark}, is_deleted = #{isDeleted} WHERE student_id = #{studentID}")
void updateStudent(Student student);

// 受講生登録
@Update("UPDATE students_Courses SET course_name = #{courseName} WHERE course_id = #{courseID}")
void updateStudentsCourses(StudentsCourses studentsCourses);

}

//  @Select("SELECT * FROM students WHERE name = #{name}")
//  Student searchByName(String name);  //ここをListに変更する↑

//  @Select("SELECT * FROM students")
//  List<Student> findAll(); // 一覧取得

//  @Insert("INSERT students values(#{name}, #{age})")
//  void registerStudent(String name, int age); // MySQLへ新情報を追加
//
//  @Update("UPDATE students SET age = #{age} WHERE name = #{name}")
//  void updatestudent(String name, int age); // 情報更新、変更
//
//  @Delete("DELETE FROM students WHERE name = #{name}")
//  void deleteStudent(String name); // 個別情報削除
//}
