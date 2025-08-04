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