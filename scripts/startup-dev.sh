docker run -d -p 9042:9042 --rm --name cassandra -v cassandra-dev:/var/lib/cassandra cassandra:latest
sleep 5
java -jar whost
#todo - echo some status messages!