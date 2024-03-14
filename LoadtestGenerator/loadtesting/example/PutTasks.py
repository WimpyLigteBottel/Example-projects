import random

from locust import task, TaskSet

from Utils import readFile, validateResponse


class PutTasks(TaskSet):
    # Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    put_v1_hello = readFile("csvs/put_v1_hello.csv")

    @task(10)
    def controllerOne_putHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        body = random.choice(self.put_v1_hello['body'])

        # Groups the request under pattern
        self.client.request_name = "v1/hello"

        # actual request
        response = self.client.put("v1/hello", data=body, headers=self.defaultHeaders)

        validateResponse(response, 200, "PUT DONE!")

        # Remove the grouping name for other request
        self.client.request_name = None