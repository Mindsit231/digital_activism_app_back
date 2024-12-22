INSERT INTO community (name, description, banner_name, logo_name)
VALUES ('Test Community 0', 'This is a test 0 community', 'test-banner.png', 'test-logo.png'),
       ('Test Community 1', 'This is a test 1 community', 'test-banner.png', 'test-logo.png');

DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR i IN 2..500
            LOOP
                INSERT INTO community (name, description)
                VALUES ('Test Community ' || i, 'This is a test ' || i || ' community');
            END LOOP;
    END
$$;

