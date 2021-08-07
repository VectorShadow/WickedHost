echo Starting database container...
docker run -d -p 9042:9042 --rm --name cassandra -v cassandra-dev:/var/lib/cassandra cassandra:latest
echo Waiting for database to initialize...
sleep 30
echo Starting WickedHost...
java -jar whost &