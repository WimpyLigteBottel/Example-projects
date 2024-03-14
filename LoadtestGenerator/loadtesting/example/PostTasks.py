import random

from locust import task, TaskSet

from Utils import readFile, validateResponse


class PostTasks(TaskSet):
    # Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    post_v1_hello = readFile("csvs/post_v1_hello.csv")

    @task(3)
    def controllerOne_postHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        body = random.choice(self.post_v1_hello['body'])

        # Groups the request under pattern
        self.client.request_name = "v1/hello"

        # actual request
        response = self.client.post("v1/hello", data=body, headers=self.defaultHeaders)

        validateResponse(response, 200, "POST DONE!")

        # Remove the grouping name for other request
        self.client.request_name = None
