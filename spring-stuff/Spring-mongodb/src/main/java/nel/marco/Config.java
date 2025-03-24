package nel.marco;

import nel.marco.db.mongo.CustomerMongoRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = CustomerMongoRepository.class)
public class Config {}
