-- name: get-all-shouts
-- Gets all the shouts
SELECT *
FROM shouts
ORDER BY id DESC;

--name: clear-shouts!
DELETE FROM shouts
