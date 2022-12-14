INSERT INTO groups (name, description) VALUES ('user', 'Permissions pertaining to the user service');

INSERT INTO groups (name, description) VALUES ('art', 'Permissions pertaining to the art service');

INSERT INTO groups (name, description) VALUES ('payment', 'Permissions pertaining to the payment service');

INSERT INTO groups (name, description) VALUES ('notification', 'Permissions pertaining to the notification service');

INSERT INTO permissions (name, description, group_id) VALUES (
'user:verify',
'Permission to verify a user',
(SELECT id FROM groups WHERE name = 'user')
);

INSERT INTO roles (name, description) VALUES ('ROLE_ADMIN', 'Role for users that carry out administrative functions on the application');

INSERT INTO permissions_roles (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
(SELECT id FROM permissions WHERE name = 'user:verify')
);