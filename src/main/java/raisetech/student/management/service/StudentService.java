package raisetech.student.management.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
//    return repository.search();

    // 検索処理
//    repository.search();

    // 絞り込みをする。年齢が30代の人のみを抽出する。
    // 抽出したリストをコントローラに渡す。　
    List<Student> all = repository.search();

    List<Student> thirties = all.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() <= 39)
        .collect(Collectors.toList());
      return thirties;
  }

  public List<StudentsCourses> searchStudentsCourseList() {
//    return repository.searchStudentsCourses();

    // 絞り込み検索で、「Javaコース」のコース情報のみを抽出する。
    // 抽出したリストをコントローラに渡す。　
    List<StudentsCourses> allCourses = repository.searchStudentsCourses();

    List<StudentsCourses> javaCourses = allCourses.stream()
//        .filter(studentsCourses  -> studentsCourses.getCourseName().contains("Java"))
        // 大文字小文字の区別をしない。　また、studentsCoursesと入力する必要はない
        .filter(course -> course.getCourseName().toLowerCase().contains("java"))
        .collect(Collectors.toList());
      return javaCourses;
  }
}
