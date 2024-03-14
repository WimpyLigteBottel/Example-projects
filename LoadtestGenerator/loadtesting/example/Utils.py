import pandas as pd


def read_file(filename):
    """
    Reads the file based on the file name and return it back as dataframe.
    You can then access that the data based on the column names df['columnA'] to get it back as list

    :param filename: The local path to the file from current directory
    :return: panda dataframe
    """
    try:
        # Read the CSV file into a DataFrame
        df = pd.read_csv(filename, sep=';')
        return df
    except FileNotFoundError:
        print("File not found. Please make sure the file exists.")
        return None



def validate_response(response, expectedStatus, expectedBody):
    """
    Validates the http response received back.

    :param response: The http response
    :param expectedStatus: the expected http status
    :param expectedBody:  the expected body response
    :return: void
    """
    assert response.status_code == expectedStatus, f"Expected status code {expectedStatus}, but received {response.status_code}"
    assert response.text == expectedBody, f"Expected body '{expectedBody}', but received {response.text}"
