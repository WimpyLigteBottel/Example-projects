package nel.marco.db.jpa;

import nel.marco.db.entity.Customer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpa extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {}
