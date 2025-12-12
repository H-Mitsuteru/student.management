package raisetech.student.management.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

public enum CourseStatus {
  仮申込み,
  本申込み,
  受講中,
  受講終了;

  @JsonCreator
  public static CourseStatus fromString(String value) {
    if (value == null) return null;

    // 正しい値と完全一致ならそのまま返す
    for (CourseStatus status : CourseStatus.values()) {
      if (status.name().equals(value)) {
        return status;
      }
    }

    // ここで表記ゆれ補正を行う（自由に追加してOK）
    switch (value.trim()) {
      case "仮申し込み":   // ← Postmanで誤字ったやつ
      case "仮申込":
        return 仮申込み;

      case "本申し込み":
      case "本申込":
        return 本申込み;

      case "終了":
        return 受講終了;

      case "受講開始":
        return 受講中;
    }

    // どれにも該当しなければエラー
    throw new IllegalArgumentException(
        "指定された status (" + value + ") は無効です。使用可能な値: "
            + Arrays.toString(CourseStatus.values())
    );
  }
}


//public enum CourseStatus {
//
//  仮申込み("仮申込み"),
//  本申込み("本申込み"),
//  受講中("受講中"),
//  受講終了("受講終了");
//
//  private final String label;
//
//  CourseStatus(String label) {
//    this.label = label;
//  }
//
//  public String getLabel() {
//    return label;
//  }
//
//}
