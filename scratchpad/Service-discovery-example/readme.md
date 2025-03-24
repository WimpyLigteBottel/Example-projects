This is very crude/basic discovery service.

It assumes that discovery service is running locally with your other service.


This can of course be change but i just wanted to setup something quickly without thinking too much about it.


If you want you service to be access via the discovery service just do the following


Use the following class:
```
ServiceDiscoveryStartupUtil.class 

METHOD:
public static void registerService(String localServerPort, String discoveryServicePort, String applicationName) 
```

NOTE: I have teamed this project up with URLshorter which is also in this git repo
