package nel.marco;

import nel.marco.db.entity.Customer;
import nel.marco.db.mongo.CustomerMongoRepository;
import nel.marco.db.mongo.DbSequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class Application implements CommandLineRunner {

  @Autowired CustomerMongoRepository customerMongoRepository;

  @Autowired DbSequenceGenerator dbSequenceGenerator;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... args) {

    for (long i = 0; i < 100; i++) {
      customerMongoRepository.save(createRandomCustomer(i));
    }
  }

  public Customer createRandomCustomer(Long id) {

    Customer customer = new Customer();

    customer.setId(dbSequenceGenerator.getNextSequence("customer.id"));
    customer.setAge(ThreadLocalRandom.current().nextInt(1, 100));
    customer.setName(getRandomName());

    int randomInt = ThreadLocalRandom.current().nextInt(0, Customer.ActiveType.values().length - 1);
    customer.setActiveType(Customer.ActiveType.values()[randomInt]);

    // 50% of the time add bank details
    if (ThreadLocalRandom.current().nextInt(1, 100) > 50) {
      Customer.BankDetails bankDetails = new Customer.BankDetails();
      bankDetails.setType(Customer.ActiveType.values()[randomInt].toString());
      bankDetails.setAccountNumber(ThreadLocalRandom.current().nextLong(1000000, 1000000000) + "");

      customer.setBankDetails(bankDetails);
    }

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
