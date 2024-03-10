import random
import pandas as pd
from locust import HttpUser, LoadTestShape, TaskSet, constant, task

import math

def readFile(fileName):
    try:
        # Read the CSV file into a DataFrame
        df = pd.read_csv(fileName, sep=';')
        return df
    except FileNotFoundError:
        print("File not found. Please make sure the file exists.")
        return None



class UserTasks(TaskSet):

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

class WebsiteUser(HttpUser):
    wait_time = constant(1)  # wait time between requests, in seconds
    tasks = [UserTasks]

class StagesShape(LoadTestShape):
    """
    A simply load test shape class that has different user and spawn_rate at
    different stages.

    Keyword arguments:

        stages -- A list of dicts, each representing a stage with the following keys:
            duration -- When this many seconds pass the test is advanced to the next stage
            users -- Total user count
            spawn_rate -- Number of users to start/stop per second
            stop -- A boolean that can stop that test at a specific stage

        stop_at_end -- Can be set to stop once all stages have run.
    """

    stages = [
        {"duration": 10, "users": 10, "spawn_rate": 1},
        {"duration": 20, "users": 20, "spawn_rate": 2},
        {"duration": 30, "users": 40, "spawn_rate": 4},
        {"duration": 40, "users": 80, "spawn_rate": 8},
        {"duration": 50, "users": 120, "spawn_rate": 16},
        {"duration": 60, "users": 240, "spawn_rate": 32},
    ]

    def tick(self):
        run_time = self.get_run_time()

        for stage in self.stages:
            if run_time < stage["duration"]:
                tick_data = (stage["users"], stage["spawn_rate"])
                return tick_data

        return None