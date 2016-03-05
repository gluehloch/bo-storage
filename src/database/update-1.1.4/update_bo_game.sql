ALTER TABLE bo_game ADD COLUMN (bo_datetime DATETIME);

SELECT
    id,
    bo_date,
    SUBSTRING(bo_date, 1, 2) AS day,
    SUBSTRING(bo_date, 4, 2) AS month,
    SUBSTRING(bo_date, 7, 4) AS year
FROM
    bo_game
WHERE
    id > 4040
;

UPDATE
    bo_game
SET
    bo_datetime = CONCAT(
        SUBSTRING(bo_date, 7, 4),
        '-',
        SUBSTRING(bo_date, 4, 2),
        '-',
        SUBSTRING(bo_date, 1, 2)
    )
;
