docker run -d -p 9042:9042 --rm --name cassandra -v cassandra-beta:var/lib/cassandra cassandra:latest
sleep 60
java -jar whost