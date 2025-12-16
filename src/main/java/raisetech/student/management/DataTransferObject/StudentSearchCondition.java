package raisetech.student.management.DataTransferObject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import raisetech.student.management.data.CourseStatus;

@Getter
@Setter
@Schema(description = "受講生検索条件")

public class StudentSearchCondition {

  @Schema(description = "受講生ID（完全一致）", example = "36")
  private String studentID;

  @Schema(description = "受講生名（部分一致）", example = "山田")
  private String name;

  @Schema(description = "メールアドレス（部分一致）", example = "example.com")
  private String email;

  @Schema(description = "居住市区町村（部分一致）", example = "横浜市")
  private String liveMunicipality;

  @Schema(description = "コース名（部分一致）", example = "Java")
  private String courseName;

  @Schema(description = "受講ステータス", example = "仮申込み",
      allowableValues = {
          "仮申込み", "仮申し込み", "仮申込",
          "本申込み", "本申し込み",
          "受講中", "受講終了"
      }
  )
  private CourseStatus status;

  /** AND / OR（未指定は AND） */
  @Schema(description = "検索条件の結合方法（未指定時は AND）", example = "AND",
      allowableValues = {"AND", "OR"}
  )

  private SearchMode mode;  /* = SearchMode.AND; */
}
