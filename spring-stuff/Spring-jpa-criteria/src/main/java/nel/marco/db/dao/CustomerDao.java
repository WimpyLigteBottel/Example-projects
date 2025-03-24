package nel.marco.db.dao;

import nel.marco.db.entity.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerDao {

  @PersistenceContext EntityManager entityManager;

  public List<Customer> find(Map<String, Object> filters, int maxResult, int offset) {

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
    Root<Customer> customerRoot = query.from(Customer.class);

    CriteriaQuery<Customer> criteriaQuery = query.select(customerRoot);

    List<Predicate> predicateList = new ArrayList<>();
    for (Map.Entry<String, Object> entry : filters.entrySet()) {

      String fieldName = entry.getKey();
      Object fieldValue = entry.getValue();

      if (fieldName.equalsIgnoreCase("name")) {
        predicateList.add(cb.like(customerRoot.get(fieldName), "%" + fieldValue + "%"));
      } else {
        predicateList.add(cb.equal(customerRoot.get(fieldName), fieldValue));
      }
    }

    criteriaQuery.where(predicateList.toArray(Predicate[]::new));

    return entityManager
        .createQuery(criteriaQuery)
        .setFirstResult(offset)
        .setMaxResults(maxResult)
        .getResultList();
  }
}
