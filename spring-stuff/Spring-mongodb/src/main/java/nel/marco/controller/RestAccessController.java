package nel.marco.controller;

import lombok.extern.slf4j.Slf4j;
import nel.marco.db.entity.Customer;
import nel.marco.db.mongo.CustomerMongoRepository;
import nel.marco.db.mongo.filter.CustomerFilter;
import nel.marco.db.mongo.filter.CustomerFilterQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class RestAccessController {

  @Autowired CustomerMongoRepository customerMongoRepository;
  @Autowired MongoTemplate mongoTemplate;

  @GetMapping("/hello")
  public void method() {

    log.info("Hello world!");
  }

  @GetMapping("/customer")
  public ResponseEntity<List<Customer>> find(
      @RequestParam(required = false) String id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Integer age,
      @RequestParam(required = false) String activeType,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false ,defaultValue = "true" )boolean asc,
      @RequestParam(required = false, defaultValue = "1000") int maxResult) {

    CustomerFilter customerFilter = new CustomerFilter();
    customerFilter.setAge(age);
    customerFilter.setId(id);
    customerFilter.setName(name);
    customerFilter.setActiveType(activeType);
    customerFilter.setMaxResults(maxResult);
    customerFilter.setSortBy(sortBy);
    customerFilter.setAscending(asc);

    Query query = new CustomerFilterQueryBuilder(customerFilter).build();

    List<Customer> customers = mongoTemplate.find(query, Customer.class);

    return ResponseEntity.ok(customers);
  }

  @GetMapping("/customers")
  public ResponseEntity<List<Customer>> find() {
    List<Customer> customer = mongoTemplate.findAll(Customer.class);

    return ResponseEntity.ok(customer);
  }
}
