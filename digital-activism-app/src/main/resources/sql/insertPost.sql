DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR i IN 1..20
            LOOP
                INSERT INTO post (community_id, content, creation_date, title, visibility)
                VALUES (1, 'This is a test post ' || i, NOW(), 'Test Post ' || i, 'PUBLIC');
                INSERT INTO post (community_id, content, creation_date, title, visibility)
                VALUES (1, 'This is a test post ' || (20 + i), NOW(), 'Test Post ' || i, 'PRIVATE');
            END LOOP;
    END
$$;

INSERT INTO post_image (name, post_id)
VALUES
    ('img-test.png', 1),
    ('img-test.png', 1);

INSERT INTO post_video (name, post_id)
VALUES
    ('video-test.mp4', 1),
    ('video-test.mp4', 1);
