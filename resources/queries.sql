-- name: get-all-shouts
-- Gets all the shouts
SELECT *
FROM shouts
ORDER BY id DESC;

--name: clear-shouts!
DELETE FROM shouts

-- name: create-shouts!
-- Create new shouts table
CREATE TABLE IF NOT EXISTS shouts
(id SERIAL PRIMARY KEY,
body VARCHAR NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);

-- name: add-shout!
-- Add a shout to the shouts table
INSERT INTO shouts (body)
VALUES (:body);

-- name: get-user
-- Get a user from the users table with a given email address
SELECT *
FROM users
WHERE email=:email;

-- name: create-user!
-- Add a user to the users table
INSERT INTO users (uuid, first_name, last_name, email)
VALUES (:uuid, :first_name, :last_name, :email);
