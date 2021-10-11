package nel.marco.db.filter;

import nel.marco.db.entity.Customer;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification implements Specification<Customer> {


    private final CustomerFilter customerFilter;

    public CustomerSpecification(CustomerFilter customerFilter) {
        this.customerFilter = customerFilter;
    }


    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();


        if (customerFilter.getId() != null) {
            predicates.add(cb.equal(root.get("id"), customerFilter.getId()));
            return cb.and(predicates.toArray(new Predicate[0]));
        }

        if (customerFilter.getName() != null) {
            predicates.add(cb.equal(root.get("name"), customerFilter.getName()));
        }

        if (customerFilter.getAge() != null) {
            predicates.add(cb.equal(root.get("age"), customerFilter.getAge()));
        }

        if (customerFilter.getActiveType() != null) {
            predicates.add(cb.equal(root.get("activeType"), customerFilter.getActiveType()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
