package raisetech.student.management;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourses {

  private String studentID;   // アンダーバーはキャメルケースに変換する命名ルールの為入力しない
  private String name;
  private String courseName;
  private Date startDate;
  private Date endDate;

}
