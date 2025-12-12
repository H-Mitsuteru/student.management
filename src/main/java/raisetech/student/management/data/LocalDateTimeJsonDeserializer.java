package raisetech.student.management.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {

    String raw = parser.getText();
    if (raw == null) return null;

    // 1) trim 全体、2) 全角スペース等を半角に（必要なら拡張）、3) 時刻部の前後の余分な空白を除去
    String set = raw.trim();

    // normalize: collapse multiple spaces to single
    set = set.replaceAll("\\s+", " ");

    // remove spaces around colons and around 'T' and around 'Z' if any
    set = set.replaceAll("\\s*:\\s*", ":");
    set = set.replaceAll("T\\s*", "T");
    set = set.replaceAll("\\s*T", "T"); // in case space before T
    set = set.replaceAll("\\s*Z\\s*$", "Z"); // keep trailing Z if present

    // now try parsing by trying formatters one by one
    for (DateTimeFormatter fmt : FORMATTERS) {
      try {
        String pattern = fmt.toString(); // not used, just keep loop
        // if formatter contains time part (has H) and input does not contain time, skip
        boolean expectsTime = fmt.toString().contains("H");
        if (!expectsTime && (set.contains(":") || set.toUpperCase().contains("T"))) {
          // input has time, but formatter is date-only; parsing still may work for some formatters, so let it try
        }

        // If formatter is date-only, parse to LocalDate and convert
        if (!fmt.toString().contains("H")) {
          // Attempt parse as LocalDate
          try {
            LocalDate date = LocalDate.parse(set, fmt);
            return date.atStartOfDay();
          } catch (Exception exception) {
            // ignore and continue
          }
        } else {
          try {
            return LocalDateTime.parse(set, fmt);
          } catch (Exception exception) {
            // ignore and continue
          }
        }
      } catch (Exception ignored) {
      }
    }

    // as a last resort try ISO parse (handles offsets etc.)
    try {
      return LocalDateTime.parse(set);
    } catch (Exception ex) {
      // give a helpful error message
      throw new IllegalArgumentException("日付の形式が不正です: " + raw);
    }
  }
}



