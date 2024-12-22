DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR i IN 1..20
            LOOP
                INSERT INTO post (community_id, member_id, content, creation_date, title, visibility)
                VALUES (1, 2, 'This is a test post ' || i, NOW(), 'Test Post ' || i, 'PUBLIC');
                INSERT INTO post (community_id, member_id, content, creation_date, title, visibility)
                VALUES (1, 2, 'This is a test post ' || (20 + i), NOW(), 'Test Post ' || i, 'PRIVATE');
            END LOOP;
    END
$$;

INSERT INTO post_image (name, post_id)
VALUES
    ('img-test.png', 1),
    ('img-test.png', 1),
    ('img-test.png', 1),
    ('img-test.png', 1);

INSERT INTO post_video (name, post_id)
VALUES
    ('video-test.mp4', 1),
    ('video-test.mp4', 1);

DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR j IN 1..5
            LOOP
                INSERT INTO tag (name)
                VALUES ('Tag ' || j);
            END LOOP;
    END
$$;

DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR i IN 1..20
            LOOP
                FOR j IN 1..5
                    LOOP
                        INSERT INTO post_tag (post_id, tag_id)
                        VALUES (i, j);
                    END LOOP;
            END LOOP;
    END
$$;

DO
$$
    DECLARE
        i INT;
    BEGIN
        FOR i IN 1..20
            LOOP
                FOR j IN 1..20
                    LOOP
                        INSERT INTO liked_post (member_id, post_id)
                        VALUES (j, i);
                    END LOOP;
            END LOOP;
    END
$$;
