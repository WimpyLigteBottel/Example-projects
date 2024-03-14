from locust import task, TaskSet

from Utils import read_file, validate_response


class DeleteTasks(TaskSet):
    # Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    delete_v1_hello = read_file("csvs/delete_v1_hello.csv")

    @task(2)
    def controllerOne_deleteHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        row = self.delete_v1_hello.sample(n=1)

        id = row['id'].iloc[0]
        name = row['name'].iloc[0]

        # Groups the request under pattern
        self.client.request_name = "v1/hello/{id}"

        # actual request
        response = self.client.delete("v1/hello/{0}".format(id), headers=self.defaultHeaders)

        validate_response(response, 200, "DELETE DONE!")

        # Remove the grouping name for other request
        self.client.request_name = None
