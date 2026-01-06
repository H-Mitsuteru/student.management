package raisetech.student.management.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {

  private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
      // 許可する日付形式 を定義
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
      DateTimeFormatter.ofPattern("yyyy-MM-dd"),
      DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy/M/d HH:mm"),
      DateTimeFormatter.ofPattern("yyyy/M/d"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
      DateTimeFormatter.ofPattern("yyyy/MM/dd")
  );

  @Override
  // JSON文字列をLocalDateTimeに変換する処理
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {

    String raw = parser.getText();
    if (raw == null) return null;

    // 1) trim ：前後の空白を除去、2) 全角スペース等を半角に（必要なら拡張）、3) 時刻部の前後の余分な空白を除去
    String set = raw.trim();

    // normalize: collapse multiple spaces to single：連続する空白を1個にまとめる
    set = set.replaceAll("\\s+", " ");

    // remove spaces around colons and around 'T' and around 'Z' if any：指定文字周りの空白を除去
    set = set.replaceAll("\\s*:\\s*", ":");
    set = set.replaceAll("T\\s*", "T");
    set = set.replaceAll("\\s*T", "T"); // in case space before T
    set = set.replaceAll("\\s*Z\\s*$", "Z"); // keep trailing Z if present：この Z は UTC（世界標準時）を示す記号

    // now try parsing by trying formatters one by one：定義したFormatを順番に試す
    for (DateTimeFormatter fmt : FORMATTERS) {
      try {
        String pattern = fmt.toString(); // not used, just keep loop
        // if formatter contains time part (has H) and input does not contain time, skip
        boolean expectsTime = fmt.toString().contains("H");
        if (!expectsTime && (set.contains(":") || set.toUpperCase().contains("T"))) {
          // input has time, but formatter is date-only; parsing still may work for some formatters, so let it try
        }

        // If formatter is date-only, parse to LocalDate and convert：日付だけ？時刻(H)あり？
        if (!fmt.toString().contains("H")) {
          // Attempt parse as LocalDate：日付のみ
          try {

            LocalDate date = LocalDate.parse(set, fmt);
            return date.atStartOfDay(); // その日の開始時刻を付与する："2025-12-06" -> "2025-12-06T00:00:00 へ"
          } catch (Exception exception) {
            // ignore and continue
          }
        } else {
          try {
            return LocalDateTime.parse(set, fmt); // 例："2025-12-06 10:30" -> "2025-12-06T10:30:00"
          } catch (Exception exception) { // 例外が発生しても何もしない、次へ
            // ignore and continue
          }
        }
      } catch (Exception ignored) { // 失敗しても次のformatを試す
      }
    }

    // as a last resort try ISO parse (handles offsets etc.)
    try {
      return LocalDateTime.parse(set); // 標準ISO形式：2025-12-06T10:30:00や2025-12-06T10:30:00.123を試す
    } catch (Exception ex) {
      // give a helpful error message：どれにも当てはまらない場合はエラー表示
      throw new IllegalArgumentException("日付の形式が不正です: " + raw);
    }
  }
}



