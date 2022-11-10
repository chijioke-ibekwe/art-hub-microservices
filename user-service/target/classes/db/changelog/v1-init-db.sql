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

alter table users add constraint uk_username unique (username);

alter table permissions add constraint fk_permissions_groups foreign key (group_id) references groups;

alter table permissions_roles add constraint fk_permissions_roles_permissions foreign key (permission_id) references permissions;

alter table permissions_roles add constraint fk_permissions_roles_roles foreign key (role_id) references roles;

alter table users_roles add constraint fk_users_roles_roles foreign key (role_id) references roles;

alter table users_roles add constraint fk_users_roles_users foreign key (user_id) references users;