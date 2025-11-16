package raisetech.student.management.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// @ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * TestException の共通ハンドリング
   */
  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex) {
    logger.error("TestException 発生", ex); // ★ログ出力を追加
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  /**
   * その他の例外（RuntimeExceptionなど）
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    logger.error("予期しないエラーが発生", ex); // ★ログ出力を追加
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("予期せぬエラーが発生しました: " + ex.getMessage());
  }
}
