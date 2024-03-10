# How to run this?


1. startup the example-spring-boot service so that it runs on port 8080
2. Then build in `/loadtesting` the docker-compose file
   1. `docker-compose build`
   2. `docker-compose up`

you should see something like this

```
Response time percentiles (approximated)
2024-03-10T20:34:12.199600872Z Type     Name                                                                                  50%    66%    75%    80%    90%    95%    98%    99%  99.9% 99.99%   100% # reqs
2024-03-10T20:34:12.199664772Z --------|--------------------------------------------------------------------------------|--------|------|------|------|------|------|------|------|------|------|------|------
2024-03-10T20:34:12.199749493Z GET      v1/hello                                                                                2      2      2      2      2      2      2      2      2      2      2     17
2024-03-10T20:34:12.199781693Z POST     v1/hello                                                                                2      2      2      2      2     13     13     13     13     13     13     18
2024-03-10T20:34:12.199948023Z PUT      v1/hello                                                                                2      2      2      2      3      3      4      4      4      4      4     25
2024-03-10T20:34:12.199996503Z --------|--------------------------------------------------------------------------------|--------|------|------|------|------|------|------|------|------|------|------|------
2024-03-10T20:34:12.200090039Z          Aggregated                                                                              2      2      2      2      2      3      4     13     13     13     13     60
2024-03-10T20:34:12.200176341Z 

```

If you dont then that means that you might need update the ip in docker-compose file i was using http://192.168.1.201:8080/ but 
you need to use your real ip of host machine not docker to allow docker container to find host ran machine