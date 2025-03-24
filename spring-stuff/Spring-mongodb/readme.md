To get this project up and running you will need

- Docker 
- java 17

Step 1: run

```docker
docker run -p 27017:27017 -d --name local-mongodb mongo:latest 
```

Step 2: Run spring application

- On start up there should be data being entered into the database

Step 3: you can call the following in your browser to see that data


```REST
http://localhost:8080/customer?id=0
```

Example:
```
{
  "id": "0",
  "name": "Adelina Aldred",
  "age": 44,
  "created": null,
  "updated": null,
  "bankDetails": {
    "accountNumber": "563181389",
    "type": "ACTIVE"
  },
  "activeType": "ACTIVE"
}
```