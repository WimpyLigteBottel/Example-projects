This is very basic url shortner that i wrote that can short url.

NOTE: I have teamed this project up with Service discovery but is not "necessary needed" and just remove the specific
code line and remove the import

Example:

```java

// REMOVE THE IMPORT FROM THE CLASS

@Component
class StartupSetup implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // COMMENT THIS LINE OUT >>>> ServiceDiscoveryStartupUtil.registerService(serverPort, discoveryServicePort, applicationName);
    }
}

```