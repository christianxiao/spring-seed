-- insert sample data
insert into users (loginId,password) values ('user1','123'),('user2','123'),('user3','123'),('admin','123');
insert into groups (name) values ('GROUP1'),('GROUP2'),('GROUP3'),('GROUP_ADMIN');
insert into user_group (groupId,userId) values (1,1),(2,2),(3,3),(4,4);
insert into roles (name) values ('ROLE1'),('ROLE2'),('ROLE3'),('ROLE4');
insert into group_role (groupId,roleId) values (1,1),(2,2),(3,3),(4,4);
insert into menus (name) values ('menu1'),('menu2');
insert into role_menu (roleId,menuId) values (2,1),(3,2);
--
insert into phones (os,userId) values ('android',2),('ios',2);
insert into employeeCards (department,userId) values ('it',2),('r&d',3);
