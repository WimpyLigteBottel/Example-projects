package nel.marco.db.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table
@Data
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @Column private String name;
  @Column private int age;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date created;

  @Column
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date updated;

  @Enumerated(EnumType.STRING)
  private ActiveType activeType;

  public enum ActiveType {
    ACTIVE,
    DEACTIVATED,
    SUSPENDED
  }
}
