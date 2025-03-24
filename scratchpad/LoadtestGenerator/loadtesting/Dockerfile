FROM docker.io/library/loadtestgenerator_marcolocust:latest

# Copy the entire current directory into the Docker image
COPY . .
COPY csvs ./csvs

RUN pip3 install pandas