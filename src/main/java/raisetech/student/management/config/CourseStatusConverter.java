package raisetech.student.management.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import raisetech.student.management.data.CourseStatus;

/**
 * 受講生コース申込み状況（CourseStatus）用の型変換クラスです。
 * リクエストパラメータ（Query Parameter）で渡された文字列を
 * CourseStatus enum に変換するために使用されます。
 * 主な用途：
 * <ul>
 *   <li> 検索APIの QueryParam（@ModelAttribute）</li>
 *   <li> AND / OR 条件検索時の status 指定 </li>
 * </ul>
 * この Converter を定義することで、
 * 表記ゆれ（例：「仮申込み」「仮申し込み」「仮申込」）を
 * 許容した検索が可能になります。
 * ※ @JsonCreator は JSON（@RequestBody）用の仕組みであり、
 *   QueryParam では使用されないため、本 Converter が必要です。
 */
@Component
public class CourseStatusConverter implements Converter<String, CourseStatus> {

  /**
   * String → CourseStatus への変換処理を行います。
   *
   * @param source リクエストパラメータで受け取った文字列
   * @return 変換後の CourseStatus（未指定または空文字の場合は null）
   * @throws IllegalArgumentException 指定された文字列がCourseStatus として解釈できない場合
   */
  @Override
  public CourseStatus convert(String source) {
    // パラメータ未指定時は条件なし扱いとする
    if (source == null || source.isBlank()) {
      return null;
    }
    // enum 側で定義した変換ロジックに委譲
    return CourseStatus.fromString(source);
  }
}
