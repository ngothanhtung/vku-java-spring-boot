-- create table roles (id, name)
CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);
-- create table users (id, username, password)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
-- create table users_roles (user_id, role_id)
CREATE TABLE IF NOT EXISTS users_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- I only insert if the role name does not exists, i use mysql database
INSERT INTO roles (name)
SELECT 'Administrators'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'Administrators'
);

INSERT INTO roles (name)
SELECT 'Managers'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'Managers'
);

INSERT INTO roles (name)
SELECT 'Users'
WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'Users'
);


INSERT INTO users (username, password) SELECT 'tungnt@softech.vn', '123456789' WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'tungnt@softech.vn');

INSERT INTO users_roles (user_id, role_id)
SELECT 
    u.id, r.id
FROM 
    (SELECT id FROM users WHERE username = 'tungnt@softech.vn' LIMIT 1) u,
    (SELECT id FROM roles WHERE name = 'Administrators' LIMIT 1) r
WHERE NOT EXISTS (
    SELECT 1 FROM users_roles WHERE user_id = u.id AND role_id = r.id
);