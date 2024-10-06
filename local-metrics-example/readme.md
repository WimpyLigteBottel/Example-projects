# How to get this up and working?

- Run the main application (kotlin)
    - This will fire of rest request and keep metrics
- Then startup the docker containers
  - grafana will be on port localhost:3000
  - prometheus: localhost:9090
- once you opened up grafana you might need to configure the datasource (remember to use `http://host.docker.internal:9090`) since
Its inside docker container