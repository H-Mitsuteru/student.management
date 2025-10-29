package raisetech.student.management.data;

import java.sql.Date;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private String courseID;
  private String studentID;   // アンダーバーはキャメルケースに変換する命名ルールの為入力しない
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

}
