package nel.marco.db.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSequence {

  @Id String id;
  // describes the field name as it will be represented in mongodb bson document
  // offers the name to be different than the field name of the class
  @Field("sequence_number")
  int sequence;
}
