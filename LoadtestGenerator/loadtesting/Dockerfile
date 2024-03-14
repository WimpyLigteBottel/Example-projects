FROM locustio/locust

# Copy the entire current directory into the Docker image
COPY ./example .
COPY ./example/csvs ./csvs

RUN pip3 install pandas