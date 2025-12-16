package raisetech.student.management.DataTransferObject;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "検索条件の結合方法")
public enum SearchMode {

  @Schema(description = "全ての条件を満たす（AND検索）")
  AND,

  @Schema(description = "いずれかの条件を満たす（OR検索）")
  OR
}
