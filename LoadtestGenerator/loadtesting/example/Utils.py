import pandas as pd


def readFile(filename):
    try:
        # Read the CSV file into a DataFrame
        df = pd.read_csv(filename, sep=';')
        return df
    except FileNotFoundError:
        print("File not found. Please make sure the file exists.")
        return None
