#What was this about?

I wanted to see how to package next.js app with my spring boot and see if it was 
possible and how.



# How do i do it?

1. package /compile it like normal with the following command

`    "build": "next build",` (npm run build)

2. Use the copy command `npm run copy`

    Note: i run windows so you might need to use something else for mac or linux based systems

3. in the pom directory run `mvn clean package`
4. The when you do the base url `localhost:8080` you should land on the landing page