from locust import HttpUser, LoadTestShape, constant_throughput

from DeleteTasks import DeleteTasks
from GetTasks import GetTasks
from PostTasks import PostTasks
from PutTasks import PutTasks
import logging

# Configure logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


# Define all your tasks here in this class
class WebsiteUser(HttpUser):
    wait_time = constant_throughput(1)  # wait time between requests, in seconds
    tasks = {
        GetTasks: 10,  # Adjust the weight according to your needs
        DeleteTasks: 1,  # Adjust the weight according to your needs
        PostTasks: 2,  # Adjust the weight according to your needs
        PutTasks: 3  # Adjust the weight according to your needs
    }


# Define your loadshape in here

class StagesShape(LoadTestShape):
    """
    A simple load test shape class that has different user and spawn_rate at
    different stages.

    Keyword arguments:

        stages -- A list of dicts, each representing a stage with the following keys:
            duration -- When this many seconds pass the test is advanced to the next stage
            users -- Total user count
            spawn_rate -- Number of users to start/stop per second
            stop -- A boolean that can stop that test at a specific stage

        stop_at_end -- Can be set to stop once all stages have run.
    """

    # Below is the shape of your testing (duration is in seconds)
    stages = [
        {"duration": 5, "users": 100, "spawn_rate": 20},
        {"duration": 10, "users": 200, "spawn_rate": 40},
        {"duration": 60, "users": 400, "spawn_rate": 50},
        {"duration": 80, "users": 350, "spawn_rate": 50},
        {"duration": 100, "users": 550, "spawn_rate": 50},
        {"duration": 120, "users": 300, "spawn_rate": 50},
        {"duration": 140, "users": 370, "spawn_rate": 50},
        {"duration": 160, "users": 10, "spawn_rate": 50},
        {"duration": 200, "users": 500, "spawn_rate": 50},
        {"duration": 220, "users": 500, "spawn_rate": 50},
    ]

    def tick(self):
        run_time = self.get_run_time()

        for stage in self.stages:
            if run_time < stage["duration"]:
                tick_data = (stage["users"], stage["spawn_rate"])
                return tick_data

        self.reset_time()
        run_time = self.get_run_time()

        for stage in self.stages:
            if run_time < stage["duration"]:
                tick_data = (stage["users"], stage["spawn_rate"])
                return tick_data

        return None
