-- Create a keyspace
CREATE KEYSPACE IF NOT EXISTS users WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' };

-- Create a table
CREATE TABLE IF NOT EXISTS users.login (
    username text PRIMARY KEY,
    salt text,
    hashed_password text,
    created timestamp
);