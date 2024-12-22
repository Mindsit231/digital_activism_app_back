-- Password is jRez.LZ4J9E!3Du
DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR i IN 1..20
            LOOP
                INSERT INTO member (email, email_verified, password, role, username)
                VALUES ('test' || i || '@test.com', true, '$2a$12$HWQ/LvOle3Du7XrzCrtnYOV64gkTxvIT2HT1q3ZdAmKr5nlfk75D2', 'AUTHENTICATED', 'TEST' || i);
            END LOOP;
    END
$$;

INSERT INTO community (name, description, owner_id)
VALUES ('Test Community 1', 'This is a test 1 community', 1),
         ('Test Community 2', 'This is a test 2 community', 2),
         ('Test Community 3', 'This is a test 3 community', 3),
         ('Test Community 4', 'This is a test 4 community', 4),
         ('Test Community 5', 'This is a test 5 community', 5),
         ('Test Community 6', 'This is a test 6 community', 6);

INSERT INTO member_community (member_id, community_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1);
