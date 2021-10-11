package nel.marco.db.filter;

import lombok.Data;
import nel.marco.db.entity.Customer;

@Data
public class CustomerFilter {

    private Long id;
    private String name;
    private Integer age;
    private Customer.ActiveType activeType;
}
