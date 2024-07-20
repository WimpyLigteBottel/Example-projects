# Shared Library Project

## Project Goal

The goal of this project is to demonstrate the concept of a shared library within your department.
This library will facilitate multiple services by providing a unified approach to establish connections
and retrieve necessary information. The library aims to follow the principle of establishing connections
once and then making them available to the rest of the application within your domain.

```
Example Use Cases:
ServiceA: 
 - Needs order information
 - Needs customer information
ServiceB:
 - Needs order information
 - Needs delivery information
ServiceC:
 - Needs delivery information
 - Needs customer information
```

## Components of the Shared Library
Included:

HTTP Client Generation:
- Generate HTTP clients from service contracts when integrating with another service.
- Ensure that the clients are reusable and adhere to the connection once principle.

Mapping Layer:
- Implement mappings from the consuming domain to the internal domain.
- Provide a consistent transformation layer to convert external data formats to internal representations and vice versa.

Shared domain concept:
- Everyone consuming this library will single view on what 'Order'

Excluded:

- Application-Specific Business Logic
```text
Examples:
- ServiceA's requirement to receive all strings in capital letters.
- ServiceA's request caching mechanisms.
```


## Additional Considerations

- Configuration Management: Ensure that the shared library supports external configuration to manage different service endpoints, credentials, and other connection parameters.
- Error Handling: Implement a robust error handling mechanism that can be reused by all consuming services.
- Logging: Provide a standardized logging mechanism to help with monitoring and debugging.
- Documentation: Include comprehensive documentation and examples to help other developers integrate the shared library into their services.

## Usage
Integrating the Shared Library

1.  Add Dependency: Include the shared library as a dependency in your service.
```xml
<dependency>
    <groupId>nel.marco</groupId>
    <artifactId>shared-connection-library</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
2.  Configure Clients: Set up the necessary configuration for the HTTP clients (e.g., service endpoints, credentials).
3.  Use Mappings: Utilize the mapping layer to transform data between the external and internal domains.
4.  Create the function/service code in `internal` package which other services can also use
     - Make sure there IS NOT BUSINESS LOGIC FROM YOUR APPLICATION IN THERE
5.  Annotation the code with the `@UsageMarker` so that its clear the intention of the code and what is needed to use the method


```kotlin

interface OrderService {

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationA, ApplicationEnum.ApplicationB],
        consumingServices = [OrderBasicHttpClient::class, OrderDeliveryHttpClient::class],
        whatDoesItSolve = [
            "see if the order exists",
            "find out what happened to delivery"
        ]
    )
    fun findDetailedOrder(orderId: String): Order

    @UsageMarker(
        applications = [ApplicationEnum.ApplicationB],
        consumingServices = [OrderDeliveryHttpClient::class],
        whatDoesItSolve = ["find out what happened to delivery"]
    )
    fun findDeliveryInfo(orderId: String): DeliveryInfo
}
```