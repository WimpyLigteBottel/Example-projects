from locust import task, TaskSet

from Utils import read_file, validate_response


class PutTasks(TaskSet):
    # Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    put_v1_hello = read_file("csvs/put_v1_hello.csv")

    @task(10)
    def controllerOne_putHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        row = self.put_v1_hello.sample(n=1)

        id = row['id'].iloc[0]
        body = row['body'].iloc[0]

        # Groups the request under pattern
        self.client.request_name = "v1/hello"

        # actual request
        response = self.client.put("v1/hello", data=body, headers=self.defaultHeaders)

        validate_response(response, 200, "PUT DONE!")

        # Remove the grouping name for other request
        self.client.request_name = None