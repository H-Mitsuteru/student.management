package raisetech.student.management.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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

  @Schema(description = "受講生コース申込み状況")
//  @NotBlank　/* enumなので削除 */
  private CourseStatus status;

  //  @NotBlank
  @Pattern(regexp = "^\\d+$")
  private String studentID;   // アンダーバーはキャメルケースに変換する命名ルールの為入力しない

  @NotBlank
  private String courseName;

  @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
  private LocalDateTime startDate;

  @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
  private LocalDateTime endDate;


}
