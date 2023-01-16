DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS todos;

CREATE TABLE todos (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(40) UNIQUE NOT NULL);


CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(40) UNIQUE NOT NULL,
                       priority VARCHAR(40) NOT NULL,
                       state VARCHAR(40) NOT NULL,
                       updated_at TIMESTAMP,
                       todo_id BIGINT,
                       FOREIGN KEY (todo_id) REFERENCES todos(id));