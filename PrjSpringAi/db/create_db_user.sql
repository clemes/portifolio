CREATE TABLESPACE ts_springai LOCATION '/data/ts_springai';
CREATE DATABASE db_springai TABLESPACE ts_springai;
CREATE GROUP my_users;
CREATE USER usr_springai WITH ENCRYPTED PASSWORD 'prjspringai' IN GROUP my_users;
GRANT ALL PRIVILEGES ON DATABASE db_springai TO usr_springai;
-- this need to be executed when connected to the database where the user need the privilege.
GRANT ALL ON SCHEMA public TO usr_springai;
