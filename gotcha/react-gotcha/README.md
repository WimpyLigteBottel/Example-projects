# Build backend

Builds the backend (you will require maven to be able to do this)
```bash
mvn clean package
cd '.\backend\' 
```

Below should startup application backend
```bash
java -jar .\backend\target\backend-react-gotcha-1.0-SNAPSHOT.jar
```


# Build frontend

Below will build the react app
```bash
npm run build
cd my-app
```


Below will run the react app
```bash
npm run dev
cd my-app
```