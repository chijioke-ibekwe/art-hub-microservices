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

create table if not exists oauth2_registered_client (
    id                              varchar(100) not null,
    client_id                       varchar(100) not null,
    client_id_issued_at             timestamp default current_timestamp not null,
    client_secret                   varchar(200) default null,
    client_secret_expires_at        timestamp default null,
    client_name                     varchar(200) not null,
    client_authentication_methods   varchar(1000) not null,
    authorization_grant_types       varchar(1000) not null,
    redirect_uris                   varchar(1000) default null,
    scopes                          varchar(1000) not null,
    client_settings                 varchar(2000) not null,
    token_settings                  varchar(2000) not null,
    primary key (id)
);

create table if not exists oauth2_authorization (
    id                                  varchar(100) not null,
    registered_client_id                varchar(100) not null,
    principal_name                      varchar(200) not null,
    authorization_grant_type            varchar(100) not null,
    authorized_scopes                   varchar(1000) default null,
    attributes                          text default null,
    state                               varchar(500) default null,
    authorization_code_value            text default null,
    authorization_code_issued_at        timestamp default null,
    authorization_code_expires_at       timestamp default null,
    authorization_code_metadata         text default null,
    access_token_value                  text default null,
    access_token_issued_at              timestamp default null,
    access_token_expires_at             timestamp default null,
    access_token_metadata               text default null,
    access_token_type                   varchar(100) default null,
    access_token_scopes                 varchar(1000) default null,
    oidc_id_token_value                 text default null,
    oidc_id_token_issued_at             timestamp default null,
    oidc_id_token_expires_at            timestamp default null,
    oidc_id_token_metadata              text default null,
    refresh_token_value                 text default null,
    refresh_token_issued_at             timestamp default null,
    refresh_token_expires_at            timestamp default null,
    refresh_token_metadata              text default null,
    primary key (id)
);

create table if not exists oauth2_authorization_consent (
    registered_client_id    varchar(100) not null,
    principal_name          varchar(200) not null,
    authorities             varchar(1000) not null,
    primary key (registered_client_id, principal_name)
);

alter table users add constraint uk_username unique (username);

alter table permissions add constraint fk_permissions_groups foreign key (group_id) references groups;

alter table permissions_roles add constraint fk_permissions_roles_permissions foreign key (permission_id) references permissions;

alter table permissions_roles add constraint fk_permissions_roles_roles foreign key (role_id) references roles;

alter table users_roles add constraint fk_users_roles_roles foreign key (role_id) references roles;

alter table users_roles add constraint fk_users_roles_users foreign key (user_id) references users;