package nel.marco;


import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import nel.marco.exposed.customer.CustomerServiceImpl;
import nel.marco.hidden.clients.customer.CustomerHttpClient;
import nel.marco.hidden.clients.customer.DetailedCustomerHttpClient;
import org.springframework.web.reactive.function.client.WebClient;

public class JavaClass {
    public static void main(String[] args) {
        System.out.println("Hello from java!");

        var customerService = new CustomerServiceImpl(
                new CustomerHttpClient(WebClient.builder().build()),
                new DetailedCustomerHttpClient(WebClient.builder().build())
        );


        try {
            var x = BuildersKt.runBlocking(
                    Dispatchers.getDefault(),//context to be ran on
                    (coroutineScope, continuation) -> customerService.findCustomer("id", continuation)
            );

            System.out.println(x);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

