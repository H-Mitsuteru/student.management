package raisetech.student.management;

import ch.qos.logback.core.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
    // localhost:8080
    SpringApplication.run(Application.class, args);
	}
  @GetMapping("/sayhello")  // Http://localhost:8080/sayhelloに対して実行
  public String hello() {   // hello 引数に下の言葉がreturn帰って来る
    return "Hello, World! Nice";
  }
}
