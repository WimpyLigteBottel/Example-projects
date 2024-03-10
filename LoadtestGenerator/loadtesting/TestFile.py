import random
import pandas as pd
from locust import HttpUser, task, between, constant

def readFile(fileName):
    try:
        # Read the CSV file into a DataFrame
        df = pd.read_csv(fileName, sep=';')
        return df
    except FileNotFoundError:
        print("File not found. Please make sure the file exists.")
        return None

class WebsiteUser(HttpUser):
    wait_time = constant(1)  # wait time between requests, in seconds

    #Need this otherwise wrong request content-type octet will be used
    defaultHeaders = {
        'Content-Type': 'application/json'
    }

    #Examples of reading the input data for each file (read it only once into memory for tests)

    #postV1hello = readFile("post_v1_hello.csv")


    put_v1_hello = readFile("put_v1/hello.csv")

    @task(1)
    def controllerOne_putHello(self):
        #Selects the "column" value from csv file which is read as dataframe
        name = random.choice(self.putv1/hello['Name'])

        #Groups the request under pattern
        self.client.request_name = "v1/hello"

        #actual request
        self.client.put("v1/hello",params={'name': name},headers=self.defaultHeaders)

        #Remove the grouping name for other request
        self.client.request_name = None


    post_v1_hello = readFile("post_v1/hello.csv")

    @task(1)
    def controllerOne_postHello(self):
        #Selects the "column" value from csv file which is read as dataframe
        name = random.choice(self.postv1/hello['Name'])

        #Groups the request under pattern
        self.client.request_name = "v1/hello"

        #actual request
        self.client.post("v1/hello",params={'name': name},headers=self.defaultHeaders)

        #Remove the grouping name for other request
        self.client.request_name = None


    get_v1_hello = readFile("get_v1/hello.csv")

    @task(1)
    def controllerOne_getHello(self):
        #Selects the "column" value from csv file which is read as dataframe
        name = random.choice(self.getv1/hello['Name'])

        #Groups the request under pattern
        self.client.request_name = "v1/hello"

        #actual request
        self.client.get("v1/hello",params={'name': name},headers=self.defaultHeaders)

        #Remove the grouping name for other request
        self.client.request_name = None