package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
   *
   * 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> search(); // 個別情報取得

//  @Select("SELECT * FROM students_courses")
  @Select("""
    SELECT s.student_id AS studentId,
           s.name AS name,
           sc.course_name AS courseName,
           sc.start_date AS startDate,
           sc.end_date AS endDate
    FROM students_courses sc
    JOIN students s ON sc.student_id = s.student_id
  """)
  List<StudentsCourses> searchStudentsCourses();

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
