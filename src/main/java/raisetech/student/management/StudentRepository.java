package raisetech.student.management;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM student WHERE name = #{name}")
  Student searchByName(String name); // 個別情報取得

  @Select("SELECT * FROM student")
  List<Student> findAll(); // 一覧取得

  @Insert("INSERT student values(#{name}, #{age})")
  void registerStudent(String name, int age); // MySQLへ新情報を追加

  @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
  void updatestudent(String name, int age); // 情報更新、変更

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name); // 個別情報削除
}
