INSERT INTO member (email, email_verified, password, role, username)
VALUES ('test1@test.com', true, 'jRez.LZ4J9E!3DuAB', 'AUTHENTICATED', 'TEST1'),
       ('test2@test.com', true, 'jRez.LZ4J9E!3DuAB', 'AUTHENTICATED', 'TEST2'),
       ('test3@test.com', true, 'jRez.LZ4J9E!3DuAB', 'AUTHENTICATED', 'TEST3'),
       ('test4@test.com', true, 'jRez.LZ4J9E!3DuAB', 'AUTHENTICATED', 'TEST4'),
       ('test5@test.com', true, 'jRez.LZ4J9E!3DuAB', 'AUTHENTICATED', 'TEST5'),
       ('test6@test.com', true, 'jRez.LZ4J9E!3DuAB', 'AUTHENTICATED', 'TEST6');

INSERT INTO community (name, description, owner_id)
VALUES ('Test Community', 'This is a test community', 1);

INSERT INTO member_community (member_id, community_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1);
