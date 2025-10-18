package raisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String studentID;
  private String name;
  private String furigana;
  private String nickname;
  private String email;               // アンダーバーはキャメルケースに変換する命名ルールの為入力しない
  private String liveMunicipality;    // Live_municipalityをLiveMunicipalityと大文字にしてもOK
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;

}

//  public String getName() {
//    return name;
//  }
//
//  public void setName(String name) {
//    this.name = name;
//  }
//
//  public int getAge() {
//    return age;
//  }
//
//  public void setAge(int age) {
//    this.age = age;
//  }
//}
