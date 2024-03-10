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

    put_v1_hello = readFile("put_v1_hello.csv")

    @task(1)
    def controllerOne_putHello(self):
        #Selects the "column" value from csv file which is read as dataframe
        body = random.choice(self.put_v1_hello['body'])

        #Groups the request under pattern
        self.client.request_name = "v1/hello"

        #actual request
        self.client.put("v1/hello",data=body,headers=self.defaultHeaders)

        #Remove the grouping name for other request
        self.client.request_name = None


    get_v1_hello = readFile("get_v1_hello.csv")

    @task(1)
    def controllerOne_getHello(self):
        #Selects the "column" value from csv file which is read as dataframe
        name = random.choice(self.get_v1_hello['Name'])

        #Groups the request under pattern
        self.client.request_name = "v1/hello"

        #actual request
        self.client.get("v1/hello",params={'name': name},headers=self.defaultHeaders)

        #Remove the grouping name for other request
        self.client.request_name = None


    post_v1_hello = readFile("post_v1_hello.csv")

    @task(1)
    def controllerOne_postHello(self):
        #Selects the "column" value from csv file which is read as dataframe
        body = random.choice(self.post_v1_hello['body'])

        #Groups the request under pattern
        self.client.request_name = "v1/hello"

        #actual request
        self.client.post("v1/hello",data=body,headers=self.defaultHeaders)

        #Remove the grouping name for other request
        self.client.request_name = None