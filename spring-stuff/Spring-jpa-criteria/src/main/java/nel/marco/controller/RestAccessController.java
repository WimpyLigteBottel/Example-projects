package nel.marco.controller;

import lombok.extern.slf4j.Slf4j;
import nel.marco.db.dao.CustomerDao;
import nel.marco.db.entity.Customer;
import nel.marco.db.filter.CustomerFilter;
import nel.marco.db.filter.CustomerSpecification;
import nel.marco.db.jpa.CustomerJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class RestAccessController {

  @Autowired CustomerJpa customerJpa;

  @Autowired CustomerDao customerDao;

  @GetMapping("/hello")
  public void method() {

    log.info("Hello world!");
  }

  @GetMapping("/customer")
  public ResponseEntity<List<Customer>> find(
          @RequestParam(required = false, defaultValue = "") String name,
          @RequestParam(required = false) Integer age,
          @RequestParam(required = false) String activeType,
          @RequestParam(required = false, defaultValue = "1000") int maxResult,
          @RequestParam(required = false, defaultValue = "0") int startIndex) {

    HashMap<String, Object> filters = new HashMap<>();
    if (!name.isEmpty()) {
      filters.put("name", name);
    }
    if (age != null) {
      filters.put("age", age);
    }
    if (activeType != null) {
      filters.put("activeType", Customer.ActiveType.valueOf(activeType));
    }

    return ResponseEntity.ok(customerDao.find(filters, maxResult, startIndex));
  }


  @GetMapping("/customer")
  public ResponseEntity<List<Customer>> findViaSpecification(
          @RequestParam(required = false, defaultValue = "") String name,
          @RequestParam(required = false) Integer age,
          @RequestParam(required = false) String activeType,
          @RequestParam(required = false, defaultValue = "1000") int maxResult,
          @RequestParam(required = false, defaultValue = "0") int startIndex) {


    CustomerFilter customerFilter = new CustomerFilter(null,name,age,Customer.ActiveType.valueOf(activeType));
    return ResponseEntity.ok(customerJpa.findAll(new CustomerSpecification(customerFilter)));
  }
}
