package nel.marco.jpa.criteria;

import nel.marco.db.entity.Customer;
import nel.marco.db.jpa.CustomerJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
class ApplicationTests {

  @Autowired CustomerJpa customerJpa;

  @PersistenceContext EntityManager entityManager;

  @Test
  void checkingThatCustomerCanBeCreated() {

    for (int i = 0; i < 10; i++) {
      customerJpa.save(createRandomCustomer());
    }

    assertTrue(customerJpa.findAll().size() > 0);
  }

  @Test
  void dynamicSelectQuery() {

    setupCustomers(10);

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Customer> query = cb.createQuery(Customer.class);

    Map<String, Object> dynamicFilterSearch = new HashMap<>();
    dynamicFilterSearch.put("activeType", Customer.ActiveType.ACTIVE);

    Root<Customer> customerRoot = query.from(Customer.class);
    CriteriaQuery<Customer> criteriaQuery = query.select(customerRoot);

    List<Customer> all = customerJpa.findAll();

    for (Map.Entry<String, Object> entry : dynamicFilterSearch.entrySet()) {

      String fieldName = entry.getKey();
      Object fieldValue = entry.getValue();

      criteriaQuery.where(cb.equal(customerRoot.get(fieldName), fieldValue));
    }

    List<Customer> resultList = entityManager.createQuery(criteriaQuery).getResultList();

    assertTrue(resultList.size() > 0);
  }

  public void setupCustomers(int amount) {

    for (int i = 0; i < amount; i++) {
      customerJpa.save(createRandomCustomer());
    }
  }

  public Customer createRandomCustomer() {

    Customer customer = new Customer();

    customer.setAge(ThreadLocalRandom.current().nextInt(1, 100));
    customer.setName(UUID.randomUUID().toString());

    int randomInt = ThreadLocalRandom.current().nextInt(0, Customer.ActiveType.values().length - 1);
    Customer.ActiveType value = Customer.ActiveType.values()[randomInt];
    customer.setActiveType(value);

    return customer;
  }
}
