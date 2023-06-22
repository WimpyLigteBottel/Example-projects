# This project is created to try out the saga* like pattern



1. server-main project
2. start server-payment
3. start server-order



## Happy scenario:

You will need start the process by http://localhost:8080/start?repeat=500
    
   1. `This will kick of the process to create request order`
   2. `It will fire off 1 request to create an order to orderService` == SUCCESS
   3. `It will fire off 1 request to create an order to paymentService` == SUCCESS
   4. `It will then save the "pending" states in memory`



There is fixed scheduled processor that will go through all tasks and check if all pendingActions are successful
if it is it will then remove it from the list of task and print success statement


## UNHAPPY scenario:

1. `This will kick of the process to create request order`
2. `It will fire off 1 request to create an order to orderService`  == SUCCESS
3. `It will fire off 1 request to create an order to paymentService` == FAILED 
4. `It will then save the states in memory`



There is fixed scheduled processor that will go through all tasks and check if there is an "FAILED" task.
It will then go through the other pendingActions that were succesfull and rollback those.

Once everything has been "processed" it will then remove if all the tasks were "FAILED" | "SUCCESS"