package nel.marco.db.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class Customer {

  @Id private String id;
  private String name;
  private int age;

  private Date created;
  private Date updated;

  private BankDetails bankDetails;

  private ActiveType activeType;

  public enum ActiveType {
    ACTIVE,
    DEACTIVATED,
    SUSPENDED
  }

  @Data
  public static class BankDetails {
    private String accountNumber;
    private String type;
  }
}
