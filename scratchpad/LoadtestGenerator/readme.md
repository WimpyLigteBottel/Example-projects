# How to run this?

1. Then build the docker-compose file
   1. `docker-compose build --no-cache`
   2. `docker-compose up`

you should see something like this once you gone to 8090

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



# How to get that weird image?

I build ontop of the base locust image with pandas installed to speed up locust rebuild times

```bash
docker build -t marcolocust:1.0.0 .
```


# How do i access locaust?

1. Check that the locust.conf file is not headless
2. Then you should be able to access it on localhost:8089 (double check the docker compose file)