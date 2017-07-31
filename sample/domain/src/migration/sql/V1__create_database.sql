create database if not exists `spring-seed`  DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;

create table if not exists users (
	id bigint auto_increment not null primary key,
    loginId varchar(64) not null unique,
    password varchar(512),
    employeeId varchar(64),
    name varchar(64),
    nameKr varchar(64),
    enable boolean not null default true,
    employStatus enum('EMPLOYED','UNEMPLOYED') default 'EMPLOYED',
    employType enum('REGULAR','OURTSOURCE') default 'REGULAR',
    createdAt datetime not null default current_timestamp,
    updatedAt datetime not null default current_timestamp,

    index loginId_idx (loginId)
);

create table if not exists groups (
	id bigint auto_increment not null primary key,
    name varchar(64) not null unique,

    index name_idx (name)
);

create table if not exists user_group (
	id bigint auto_increment not null primary key,
    groupId bigint not null,
    userId bigint not null,

    foreign key (groupId) references groups(id),
    foreign key (userId) references users(id),
    unique (groupId, userId)
);

create table if not exists roles (
	id bigint auto_increment not null primary key,
    name varchar(64) not null unique,
    description varchar(128),
    createdAt datetime not null default current_timestamp,
    updatedAt datetime not null default current_timestamp
);

create table if not exists group_role (
	id bigint not null auto_increment primary key,
    groupId bigint not null,
    roleId bigint not null,

    foreign key (groupId) references groups(id),
    foreign key (roleId) references roles(id),
    unique (groupId, roleId)
);

create table if not exists menus (
	id bigint not null auto_increment primary key,
    name varchar(256) not null unique,
    status enum('SHOW','HIDE','DISABLE') default 'SHOW',
    uri varchar(256) not null default '',
    httpMethod enum('GET','PUT','HEAD','POST')default 'GET',
	depth int not null default 10,
    menuOrder int not null default 10,
    parentId bigint
);

create table if not exists role_menu (
	id bigint not null auto_increment primary key,
    roleId bigint not null,
    menuId bigint not null,

    foreign key (roleId) references roles(id),
    foreign key (menuId) references menus(id),
    unique (roleId,menuId)
);

create table if not exists phones (
	id bigint not null auto_increment primary key,
    os varchar(256),
    name varchar(256),
    userId bigint,
    foreign key (userId) references users(id)
);

create table if not exists employeeCards (
	id bigint not null auto_increment primary key,
    department varchar(256),
    createdAt datetime not null default current_timestamp,
    userId bigint unique,
    foreign key (userId) references users(id)
);
