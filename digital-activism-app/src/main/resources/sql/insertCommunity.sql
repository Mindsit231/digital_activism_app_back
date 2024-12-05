INSERT INTO community (name, description, banner_name, logo_name, owner_id)
VALUES ('Test Community 0', 'This is a test 0 community', 'test-banner.png', 'test-logo.png', 1),
       ('Test Community 1', 'This is a test 1 community', 'test-banner.png', 'test-logo.png', 1);

DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR i IN 2..500
            LOOP
                INSERT INTO community (name, description, owner_id)
                VALUES ('Test Community ' || i, 'This is a test ' || i || ' community', 1);
            END LOOP;
    END
$$;

