# Maps Routing - SysDev Course project. 

Goal was to devevlop a backend server based on a given web front-end client. The backend should perform a shortest path search of 2 given points on the interface.

# Client Frontend
The client is a (google) maps like interface implemented using angular.

To run the front-end interface run the following steps:
* Install: run npm i
* Run: npm run develop

# Backend Jersey Server
The server side logic was implemented in a Java mavens project. 
* Grizzly HTTP server takes care of client's requests to perform shortest path
* Based on selected option in client google API is called or own search algorithm 
* Own implementation of algorithm resides on a further TCP server
* TCP server initiates road network graph and performs A* shortest path search

