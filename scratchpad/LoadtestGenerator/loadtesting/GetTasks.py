from locust import task, TaskSet

from Utils import read_file, validate_response


class GetTasks(TaskSet):
    # Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    get_v1_hello = read_file("csvs/get_v1_hello.csv")

    @task(1)
    def controllerOne_getHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        row = self.get_v1_hello.sample(n=1)

        id = row['id'].iloc[0]
        name = row['name'].iloc[0]

        # Groups the request under pattern #see https://docs.locust.io/en/stable/writing-a-locustfile.html#grouping-requests
        self.client.request_name = "v1/hello/{id}"

        # actual request
        response = self.client.get("v1/hello/{0}".format(id), headers=self.defaultHeaders)

        validate_response(response, 200, "GET DONE!")

        # Remove the grouping name for other request
        self.client.request_name = None


    @task(2)
    def controllerOne_getHello2(self):
        # Selects the "column" value from csv file which is read as dataframe
        row = self.get_v1_hello.sample(n=1)

        id = row['id'].iloc[0]
        name = row['name'].iloc[0]

        # Groups the request under pattern #see https://docs.locust.io/en/stable/writing-a-locustfile.html#grouping-requests
        self.client.request_name = "v1/hello/{id}2"

        # actual request
        response = self.client.get("v1/hello/{0}".format(id), headers=self.defaultHeaders)

        validate_response(response, 200, "GET DONE!")

        # Remove the grouping name for other request
        self.client.request_name = None
