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

-- name: drop-table!
-- drop the shouts table
DROP TABLE shouts;
