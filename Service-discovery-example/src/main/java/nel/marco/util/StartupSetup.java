package nel.marco.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Extend this class and @Component this service
 */
public abstract class StartupSetup implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WebServerApplicationContext applicationContext;
    private final String applicationName;
    private final String discoveryServicePort;

    public StartupSetup(@Value("${discovery.service.port}") String discoveryServicePort,
                        @Value("${application.name}") String applicationName,
                        WebServerApplicationContext applicationContext) {
        this.discoveryServicePort = discoveryServicePort;
        this.applicationName = applicationName;
        this.applicationContext = applicationContext;
    }


    @Override
    public void run(String... args) throws Exception {
        String port = String.valueOf(applicationContext.getWebServer().getPort());
        ServiceDiscoveryStartupUtil.registerService(port, discoveryServicePort, applicationName);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        String port = String.valueOf(applicationContext.getWebServer().getPort());
        ServiceDiscoveryStartupUtil.deregisterService(port, discoveryServicePort, applicationName);
    }
}