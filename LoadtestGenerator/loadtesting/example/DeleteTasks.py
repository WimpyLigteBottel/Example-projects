import random

from locust import task, TaskSet

from Utils import readFile, validateResponse


class DeleteTasks(TaskSet):
    # Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    delete_v1_hello = readFile("csvs/delete_v1_hello.csv")

    @task(2)
    def controllerOne_deleteHello(self):
        # Selects the "column" value from csv file which is read as dataframe
        id = random.choice(self.delete_v1_hello['id'])

        # Groups the request under pattern
        self.client.request_name = "v1/hello/{id}"

        # actual request
        response = self.client.delete("v1/hello/{0}".format(id), headers=self.defaultHeaders)

        validateResponse(response,200, "DELETE DONE!")

        # Remove the grouping name for other request
        self.client.request_name = None
