package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Schema(description = "受講生")
@Getter
@Setter
@ToString
public class Student {

  @Schema(description = "受講生ID自動採番")
//  @NotBlank
  @Pattern(regexp = "^\\d+$", message = "数字のみ入力するようにして下さい。")
  private String studentID;

  @NotBlank
  private String name;

  @NotBlank
  private String furigana;

  private String nickname;

  @NotBlank
  private String email;               // アンダーバーはキャメルケースに変換する命名ルールの為入力しない

  @Schema(description = "住んでいる市町村")
  @NotBlank
  private String liveMunicipality;    // Live_municipalityをLiveMunicipalityと大文字にしてもOK

  private int age;

  @Schema(description = "男、女、非公開、その他 から選択" )
  @NotBlank
  private String gender;

  private String remark;
  private boolean isDeleted;

}
