FROM docker.io/library/marcolocust:1.0.0

# Copy the entire current directory into the Docker image
COPY . .
COPY csvs ./csvs

RUN pip3 install pandas