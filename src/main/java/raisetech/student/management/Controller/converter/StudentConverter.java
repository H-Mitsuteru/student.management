package raisetech.student.management.Controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;

@Component
public class StudentConverter {
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      // 以下を選択して リファクタリング-> メソッド抽出すると上の様になる。
//      List<StudentDetail> studentDetails = new ArrayList<>();
//      students.forEach(student -> {
//        StudentDetail studentDetail = new StudentDetail();
//        studentDetail.setStudent(student);

//      List<StudentsCourses> convertStudentCourses = new ArrayList<>();
//      for (StudentsCourses studentCourses : studentsCourses) {
//        if (student.getStudentID() != null && student.getStudentID()
//            .equals(studentCourses.getStudentID())) {
//          convertStudentCourses.add(studentCourses);
//        }
//      }
      // 上の処理 collectを含むループを折りたたむと下になる。
      List<StudentsCourses> convertStudentCourses = studentsCourses.stream()
          .filter(studentCourses -> student.getStudentID() != null && student.getStudentID()
              .equals(studentCourses.getStudentID()))
          .collect(Collectors.toList());

      studentDetail.setStudentsCourses(convertStudentCourses);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }
}
