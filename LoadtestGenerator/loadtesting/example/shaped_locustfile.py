from locust import HttpUser, LoadTestShape, constant

from TasksA import TaskA


class WebsiteUser(HttpUser):
    wait_time = constant(1)  # wait time between requests, in seconds
    tasks = [TaskA]


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
        {"duration": 20, "users": 10, "spawn_rate": 5},
        {"duration": 40, "users": 20, "spawn_rate": 5},
        {"duration": 60, "users": 40, "spawn_rate": 10},
    ]

    def tick(self):
        run_time = self.get_run_time()

        for stage in self.stages:
            if run_time < stage["duration"]:
                tick_data = (stage["users"], stage["spawn_rate"])
                return tick_data

        return None