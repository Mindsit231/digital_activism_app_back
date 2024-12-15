


USE digital_app;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    image_path VARCHAR(255)
);

CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    image_path VARCHAR(255),
    user_id INT REFERENCES users(user_id)
);

CREATE TABLE user_group (
    user_group_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    group_id INT REFERENCES groups(group_id)
);

CREATE TABLE campaigns (
    campaign_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start DATE,
    end DATE
);

CREATE TABLE messages (
    message_id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    campaign_id INT REFERENCES campaigns(campaign_id),
    sender_id INT REFERENCES users(user_id)
);

CREATE TABLE message_images (
    message_image_id SERIAL PRIMARY KEY,
    image_path VARCHAR(255),
    message_id INT REFERENCES messages(message_id)
);

CREATE TABLE message_videos (
    message_video_id SERIAL PRIMARY KEY,
    video_path VARCHAR(255),
    message_id INT REFERENCES messages(message_id)
);

CREATE TABLE tags (
    tag_id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE user_tags (
    user_tag_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    tag_id INT REFERENCES tags(tag_id)
);

CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    text TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50),
    group_id INT REFERENCES groups(group_id),
    like_count INT DEFAULT 0
);

CREATE TABLE post_images (
    post_image_id SERIAL PRIMARY KEY,
    image_path VARCHAR(255),
    post_id INT REFERENCES posts(post_id)
);

CREATE TABLE post_videos (
    post_video_id SERIAL PRIMARY KEY,
    video_path VARCHAR(255),
    post_id INT REFERENCES posts(post_id)
);

CREATE TABLE post_tags (
    post_tag_id SERIAL PRIMARY KEY,
    post_id INT REFERENCES posts(post_id),
    tag_id INT REFERENCES tags(tag_id)
);

