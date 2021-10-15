# BasicUssdLikeService
This is basic ussd like service to simulate how the flow would work


#Why does this project seem incomplete?
The idea of this is just give basic idea of how ussd system would flow.

#Why so many comments in the code?
Ideally I would not leave this comments in the code if this were to go to production. 
The reason why I left them is to communicate certain things I thought about. Or where
changes should be implemented.



#How to run the project
You will need the following programs install on your system.

- Maven 3
- Java 8+
- Preffered IDE



##step 0:
If you would like to run this via IDE just run the Application.java to get started
 


##Step 1:

If you plan on not doing via the IDE you should be able to do as follows.
After checking out this project locally run the following command in ${base.dir}

mvn clean package


##Step 2:
navigate to the /target dir then run the following command

java -jar ussd-service-1.0-SNAPSHOT.jar

If you see the below you know it started up successfully.

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.4.1)

2021-01-15 20:13:52.805  INFO 6636 --- [           main] nel.marco.Application                    : Starting Application v1.0-SNAPSHOT using Java 1.8.0_221 on Marco-PC with PID 6636 (D:\coding repo\basicUssdLikeService\target\ussd-service-1.0-SNAPSHOT.jar started b
y Marco in D:\coding repo\basicUssdLikeService\target)
2021-01-15 20:13:52.808  INFO 6636 --- [           main] nel.marco.Application                    : No active profile set, falling back to default profiles: default
2021-01-15 20:13:53.922  INFO 6636 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-01-15 20:13:53.931  INFO 6636 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-01-15 20:13:53.931  INFO 6636 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.41]
2021-01-15 20:13:53.983  INFO 6636 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-01-15 20:13:53.983  INFO 6636 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1122 ms
2021-01-15 20:13:54.161  INFO 6636 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2021-01-15 20:13:54.350  INFO 6636 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-01-15 20:13:54.360  INFO 6636 --- [           main] nel.marco.Application                    : Started Application in 1.943 seconds (JVM running for 2.316)

```

