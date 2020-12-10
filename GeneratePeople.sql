DELIMITER //
DROP PROCEDURE IF EXISTS generatePeople//
CREATE PROCEDURE generatePeople(IN total INT)
BEGIN
	DECLARE randVal INT DEFAULT 0;
    DECLARE counter INT DEFAULT 0;
    DECLARE date_of_birth DATE;
    DECLARE age INT DEFAULT 0;
	DECLARE firstName VARCHAR(100) DEFAULT '';
    DECLARE lastName VARCHAR(100) DEFAULT '';
    DECLARE loopDone BOOL DEFAULT FALSE;
    DECLARE cursorCount INT DEFAULT 0;
    DECLARE cursor2 CURSOR FOR SELECT last_name FROM lastnames;
    DECLARE cursor1 CURSOR FOR SELECT first_name FROM firstnames;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET loopDone = TRUE;
    myLoop: LOOP
    	SET counter = counter +1;
        IF counter >= total THEN
        	LEAVE myLoop;
        END IF;
        OPEN cursor1;
        SET cursorCount = 0;
        SET randVal = FLOOR(RAND()*(50)+1);
        cursorLoop : LOOP
            FETCH cursor1 INTO firstName;

            IF loopDone = TRUE THEN
                LEAVE cursorLoop;
            END IF;
            SET cursorCount = cursorCount + 1;
            IF cursorCount = randVal THEN
                LEAVE cursorLoop;
            END IF;
        END LOOP;
        CLOSE cursor1;
        /*END OF PART 1*/

        SET cursorCount = 0;
        OPEN cursor2;
        SET randVal = FLOOR(RAND()*(50)+1);
        cursorLoop : LOOP
            FETCH cursor2 INTO lastName;

            IF loopDone = TRUE THEN
                LEAVE cursorLoop;
            END IF;
            SET cursorCount = cursorCount + 1;
            IF cursorCount = randVal THEN
                LEAVE cursorLoop;
            END IF;
        END LOOP;
        CLOSE cursor2;
        SET date_of_birth=CONCAT_WS('-',(FLOOR( 1950 + RAND( ) *70)),(FLOOR( 1 + RAND( ) *12 )),(FLOOR( 1 + RAND( ) *28 )));
        SET age=DATE_FORMAT(NOW(), '%Y') - DATE_FORMAT(date_of_birth, '%Y') - (DATE_FORMAT(NOW(), '00-%m-%d') < DATE_FORMAT(date_of_birth, '00-%m-%d'));
        INSERT INTO People(`lastName`, `firstName`, `dateOfBirth`, `age`) VALUES (lastName, firstName, date_of_birth, age);
    END LOOP myLoop;
END
//
DELIMITER ;