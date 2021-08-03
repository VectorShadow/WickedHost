docker run --name cassandra cassandra &
sleep 30s
#todo - figure out how to run .cql?
#docker run --rm -d --name cassandra --hostname cassandra --network cassandra cassandra
#sleep 30s
#docker run --rm --network cassandra -v "$(pwd)/data.cql:/scripts/data.cql" -e CQLSH_HOST=cassandra -e CQLSH_PORT=9042 nuvo/docker-cqlsh