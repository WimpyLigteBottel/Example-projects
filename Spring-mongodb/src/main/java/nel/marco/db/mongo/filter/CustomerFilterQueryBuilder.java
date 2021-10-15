package nel.marco.db.mongo.filter;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class CustomerFilterQueryBuilder {

  private final CustomerFilter customerFilter;

  public CustomerFilterQueryBuilder(CustomerFilter customerFilter) {
    this.customerFilter = customerFilter;
  }

  public Query build() {

    Query query = new Query();

    if (customerFilter.getId() != null) {
      query.addCriteria(Criteria.where("id").is(customerFilter.getId()));
    }
    if (customerFilter.getName() != null) {
      query.addCriteria(Criteria.where("name").is(customerFilter.getName()));
    }
    if (customerFilter.getAge() != null) {
      query.addCriteria(Criteria.where("age").is(customerFilter.getAge()));
    }
    if (customerFilter.getActiveType() != null) {
      query.addCriteria(Criteria.where("activeType").is(customerFilter.getActiveType()));
    }
    Sort sorting = Sort.by("id");
    if (customerFilter.getSortBy() != null) {
      sorting = Sort.by(customerFilter.getSortBy()).ascending();
      if (!customerFilter.getAscending()) {
        sorting = Sort.by(customerFilter.getSortBy()).descending();
      }
    }

    return query.with(sorting).limit(customerFilter.getMaxResults());
  }
}
