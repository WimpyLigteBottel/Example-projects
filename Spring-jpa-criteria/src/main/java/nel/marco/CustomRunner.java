package nel.marco;

import nel.marco.db.entity.Customer;
import nel.marco.db.jpa.CustomerJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CustomRunner implements CommandLineRunner {

  @Autowired private CustomerJpa customerJpa;

  @Override
  public void run(String... args) throws Exception {

    for (int i = 0; i < 100; i++) {
      customerJpa.save(createRandomCustomer());
    }
  }

  public Customer createRandomCustomer() {

    Customer customer = new Customer();

    customer.setAge(ThreadLocalRandom.current().nextInt(1, 100));
    customer.setName(getRandomName());

    int randomInt = ThreadLocalRandom.current().nextInt(0, Customer.ActiveType.values().length - 1);
    Customer.ActiveType value = Customer.ActiveType.values()[randomInt];
    customer.setActiveType(value);

    return customer;
  }

  public String[] getNames() {
    return "Adelina Aldred#Tianna Jarvis#Finlay Zamora#Woody Plummer#Eva Wu#Aamna Larson#Poppy-Rose Marks#Maaria Gibson#Milana Cisneros#Abby Hope"
        .split("#");
  }

  public String getRandomName() {

    String[] names = getNames();
    int index = ThreadLocalRandom.current().nextInt(0, names.length - 1);

    return names[index];
  }
}
