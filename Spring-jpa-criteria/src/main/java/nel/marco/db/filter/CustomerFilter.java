package nel.marco.db.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nel.marco.db.entity.Customer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFilter {

    private Long id;
    private String name;
    private Integer age;
    private Customer.ActiveType activeType;
}
