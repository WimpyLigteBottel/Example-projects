import random

from locust import task, TaskSet

from Utils import readFile


class TaskA(TaskSet):
    # Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    put_v1_hello = readFile("csvs/put_v1_hello.csv")

    @task(1)
    def controllerOne_putHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        body = random.choice(self.put_v1_hello['body'])

        # Groups the request under pattern
        self.client.request_name = "v1/hello"

        # actual request
        self.client.put("v1/hello", data=body, headers=self.defaultHeaders)

        # Remove the grouping name for other request
        self.client.request_name = None

    get_v1_hello = readFile("csvs/get_v1_hello.csv")

    @task(3)
    def controllerOne_getHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        name = random.choice(self.get_v1_hello['Name'])

        # Groups the request under pattern
        self.client.request_name = "v1/hello"

        # actual request
        self.client.get("v1/hello", params={'name': name}, headers=self.defaultHeaders)

        # Remove the grouping name for other request
        self.client.request_name = None

    post_v1_hello = readFile("csvs/post_v1_hello.csv")

    @task(2)
    def controllerOne_postHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        body = random.choice(self.post_v1_hello['body'])

        # Groups the request under pattern
        self.client.request_name = "v1/hello"

        # actual request
        self.client.post("v1/hello", data=body, headers=self.defaultHeaders)

        # Remove the grouping name for other request
        self.client.request_name = None
