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

INSERT INTO oauth_client_details (client_id, client_secret, resource_ids, scope, authorized_grant_types,
web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES
('ADMIN_CLIENT_APP', '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi',
'USER_CLIENT_RESOURCE,USER_ADMIN_RESOURCE', 'ROLE_ADMIN,ROLE_USER', 'password,refresh_token,implicit',
null, null, 900, 3600, '{"credential": "email"}', null);

INSERT INTO clients_roles (client_id, role_id) VALUES (
'ADMIN_CLIENT_APP',
(SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
);