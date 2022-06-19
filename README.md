# RIP V2 Simulation using Rovers

The Project was built to simulate the RIP V2 protocol using rovers. Each rover on the network executes RIP to exchange routing information with its neighbors, and based on this information, the rover computes the shortest paths from itself to all the other rovers and the lander, which for this experiment may be considered just another rover that does not move

### To build
This will also build any java files in the current directory in the container.

`docker build -t javaapptest . `

### To create the node network
Only needs to be done once.

`docker network create --subnet=172.18.0.0/16 nodenet `


### To Run (for example, node 1)
This will ultimately run the java Main class as an application.

`docker run -it -p 8080:8080 --cap-add=NET_ADMIN --net nodenet --ip 172.18.0.21 javaapptest 1 `

### To Run (node 2):
`docker run -it -p 8081:8080 --cap-add=NET_ADMIN --net nodenet --ip 172.18.0.22 javaapptest 2 `

### To Block Nodes 2 and 3 on Node 1
Using the block=ip http query parameter.

`curl "http://localhost:8080/?block=172.18.0.22&block=172.18.0.23" `

### To unblock Node 2 on Node 1
Using the unblock=ip http query parameter.

`curl "http://localhost:8080/?unblock=172.18.0.22" `

### To randomly drop 10% of incoming packets to Node 1
Using the indrop=p http query parameter.  Note that these are incoming packets -- not outgoing.  IPTABLES notifies higher layers when outgoing packets are dropped invalidating the testing.

`curl "http://localhost:8080/?indrop=0.1" `


Note that multiple commands can be appended on one URL.

# ENJOY!
