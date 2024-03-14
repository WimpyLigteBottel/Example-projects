import pandas as pd


def readFile(filename):
    try:
        # Read the CSV file into a DataFrame
        df = pd.read_csv(filename, sep=';')
        return df
    except FileNotFoundError:
        print("File not found. Please make sure the file exists.")
        return None


def validateResponse(response,expectedStatus,expectedBody):
    assert response.status_code == expectedStatus, f"Expected status code {expectedStatus}, but received {response.status_code}"
    assert response.text == expectedBody, f"Expected body '{expectedBody}', but received {response.text}"
