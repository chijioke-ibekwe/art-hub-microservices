create table groups (
    id              bigserial not null,
    description     varchar(255),
    name            varchar(255),
    primary key (id)
);

create table permissions (
    id                      bigserial not null,
    description             varchar(255),
    name                    varchar(255),
    requires_verification   boolean default false,
    group_id                int8,
    primary key (id)
);

create table permissions_roles (
    role_id         int8 not null,
    permission_id   int8 not null,
    primary key (role_id, permission_id)
);

create table roles (
    id              bigserial not null,
    description     varchar(255),
    name            varchar(255),
    primary key (id)
);

create table oauth_client_details (
    client_id                   varchar(255) not null,
    client_secret               varchar(255),
    resource_ids                varchar(255),
    scope                       varchar(255),
    authorized_grant_types      varchar(255),
    web_server_redirect_uri     varchar(255),
    authorities                 varchar(255),
    access_token_validity       int4,
    refresh_token_validity      int4,
    additional_information      varchar(255),
    autoapprove                varchar(255),
    primary key (client_id)
);

create table clients_roles (
    client_id       varchar(255) not null,
    role_id         int8 not null,
    primary key (client_id, role_id)
);

create table users (
    id                                  bigserial not null,
    first_name                          varchar(255),
    last_name                           varchar(255),
    username                            varchar(255),
    password                            varchar(255),
    phone_number                        varchar(255),
    account_non_expired                 boolean default true,
    account_non_locked                  boolean default true,
    credentials_non_expired             boolean default true,
    enabled                             boolean default true,
    verified                            boolean default false,
    can_access_authorized_permission    boolean default false,
    primary key (id)
);

create table users_roles (
    user_id         int8 not null,
    role_id         int8 not null,
    primary key (user_id, role_id)
);

alter table users add constraint uk_username unique (username);

alter table permissions add constraint fk_permissions_groups foreign key (group_id) references groups;

alter table permissions_roles add constraint fk_permissions_roles_permissions foreign key (permission_id) references permissions;

alter table permissions_roles add constraint fk_permissions_roles_roles foreign key (role_id) references roles;

alter table clients_roles add constraint fk_clients_roles_clients foreign key (client_id) references oauth_client_details;

alter table clients_roles add constraint fk_clients_roles_roles foreign key (role_id) references roles;

alter table users_roles add constraint fk_users_roles_roles foreign key (role_id) references roles;

alter table users_roles add constraint fk_users_roles_users foreign key (user_id) references users;