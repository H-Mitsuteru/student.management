package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "受講生コース情報")
@Getter
@Setter
@ToString
public class StudentCourse {

//  @NotBlank
  @Pattern(regexp = "^\\d+$")
  private String courseID;

//  @NotBlank
  @Pattern(regexp = "^\\d+$")
  private String studentID;   // アンダーバーはキャメルケースに変換する命名ルールの為入力しない

  @NotBlank
  private String courseName;

//  @NotNull
  private LocalDateTime startDate;
  private LocalDateTime endDate;
//  private LocalDateTime CourseStartAt;
//  private LocalDateTime CourseEndAt;

}
