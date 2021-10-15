package nel.marco.manager;

import nel.marco.model.Session;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentManager {

    public void payClient(List<Session> sessions) {

        try {
            //Add payment code that retrieve session details
        } catch (Exception e) {
            // if there is an error log and throw exception
        }

    }
}
