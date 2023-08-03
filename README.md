This repository contains two different project:

- backend for publishing the realtime feeds.

- frontend for displaying the realtime feeds.

`backend/demo-others` is a java project used for data that requires HTTP or RSS requests.

`backend/demo-earthquake` is a python project used for data that is obtained from a websocket connection.

For more details about each project look at the each project README file.

To run the demos and see the output in browser run the `start.sh` bash script. The script builds a docker image for each project (`demo-earthquake`, `demo-others` and `frontend`) and starts docker compose file from current directory.

The docker compose configuration starts the latest MigratoryData server instance. For each demo publisher a container is running and the agent is connecting to the MigratoryData server and publishes messages. The `frontend` container is listening to connections from the browser to display the data.

- Run the `start.sh` script and wait for the containers to start.
- Open a browser and connect to address `http://127.0.0.1:8080` to display the demos.