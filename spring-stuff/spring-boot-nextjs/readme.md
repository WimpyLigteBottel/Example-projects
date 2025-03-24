#What was this about?

I wanted to see how to package next.js app with my spring boot and see if it was 
possible and how.



# How do i do it?

1. package /compile it like normal with the following command

```bash
cd src/main/frontend
npm run build
````

Note: i run windows so you might need to use something else for mac or linux based systems because of the copy command


2. Build the backend `mvn clean package`

3. The when you do the base url `http://localhost:8080` you should land on the landing page

# How to edit frontend and run backend

Sometimes you would like to edit the frontend but still use the backend. This is easy as follows

Run the frontend
```bash
cd src/main/frontend
npm run dev
```

Use prettier to format my code
```bash
cd src/main/frontend
npm run lint
```
