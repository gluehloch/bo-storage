ALTER TABLE bo_gamelist ADD COLUMN (bo_datetime DATETIME);

SELECT
    id,
    bo_date,
    SUBSTRING(bo_date, 1, 2) AS day,
    SUBSTRING(bo_date, 4, 2) AS month,
    SUBSTRING(bo_date, 7, 4) AS year
FROM
    bo_gamelist
;

/*
 * Problem IDs:
 * 
 * 26, 27, 28, 29, 30, 31, 32, 33, 34
 * Folgendes Format: 31.05.02-02.06.02
 */
UPDATE bo_gamelist SET bo_date = '31.05.2002' WHERE id = 26;
UPDATE bo_gamelist SET bo_date = '03.06.2002' WHERE id = 27;
UPDATE bo_gamelist SET bo_date = '06.06.2002' WHERE id = 28;
UPDATE bo_gamelist SET bo_date = '09.06.2002' WHERE id = 29;
UPDATE bo_gamelist SET bo_date = '12.06.2002' WHERE id = 30;
UPDATE bo_gamelist SET bo_date = '15.06.2002' WHERE id = 31;
UPDATE bo_gamelist SET bo_date = '17.06.2002' WHERE id = 32;
UPDATE bo_gamelist SET bo_date = '21.06.2002' WHERE id = 33;
UPDATE bo_gamelist SET bo_date = '25.06.2002' WHERE id = 34;

/*
 * 35, 36 
 * Folgendes Format: 29.06.02
 */
UPDATE bo_gamelist SET bo_date = '29.06.2002' WHERE id = 35;
UPDATE bo_gamelist SET bo_date = '30.06.2002' WHERE id = 36;
 
/*
 * 49, 50
 * Folgendes Format: 24.06.2004 - 27.06.2004
 * --> Hier koennen die Werte so belassen werden!
 */
/*UPDATE bo_gamelist SET bo_date = '24.06.2004' WHERE id = 49;*/
/*UPDATE bo_gamelist SET bo_date = '30.06.2004' WHERE id = 50;*/

/* 
 * 154, 155, 156, 157
 * Folgendes Format:
 * | 154 | 12.8.2000               | 12   | 8.    | 000  |
 * | 155 | 19.8.2000               | 19   | 8.    | 000  |
 * | 156 | 5.9.2000                | 5.   | .2    | 00   |
 * | 157 | 9.9.2000                | 9.   | .2    | 00   |
 * 
 */
UPDATE bo_gamelist SET bo_date = '12.08.2000' WHERE id = 154;
UPDATE bo_gamelist SET bo_date = '19.08.2000' WHERE id = 155;
UPDATE bo_gamelist SET bo_date = '05.09.2000' WHERE id = 156;
UPDATE bo_gamelist SET bo_date = '09.09.2000' WHERE id = 157;



UPDATE
    bo_gamelist
SET
    bo_datetime = CONCAT(
        SUBSTRING(bo_date, 7, 4),
        '-',
        SUBSTRING(bo_date, 4, 2),
        '-',
        SUBSTRING(bo_date, 1, 2)
    )
;
