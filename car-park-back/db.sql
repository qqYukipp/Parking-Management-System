create table cp_menu
(
    id         bigint auto_increment comment '主键ID'
        primary key,
    menu_name  varchar(100)         not null comment '菜单名称',
    path       varchar(255)         null comment '路由路径',
    component  varchar(255)         null comment '组件路径',
    parent_id  bigint     default 0 null comment '父菜单ID',
    menu_type  varchar(20)          null comment '菜单类型：CATALOG-目录 MENU-菜单 BUTTON-按钮',
    permission varchar(100)         null comment '权限标识',
    visible    tinyint(1) default 1 null comment '是否显示',
    icon       varchar(100)         null comment '菜单图标'
)
    comment '菜单表';

INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (1, '系统首页', '/index', '../views/menu/Index.vue', -1, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (2, '数据统计', '/dashboard', '../views/menu/Dashboard.vue', -1, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (3, '信息管理', '/info', null, -1, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (4, '停车信息', '/info/parking', '../views/menu/info/Parking.vue', 3, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (5, '缴费信息', '/info/pay', '../views/menu/info/Pay.vue', 3, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (6, '停车区域', '/info/location', '../views/menu/info/Location.vue', 3, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (7, '车位信息', '/info/parkingLot', '../views/menu/info/ParkingLot.vue', 3, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (8, '车辆信息', '/info/vehicle', '../views/menu/info/Vehicle.vue', 3, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (9, '系统公告', '/info/notice', '../views/menu/info/Notice.vue', 3, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (10, '系统用户', '/systemUser', null, -1, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (11, '管理员信息', '/systemUser/admin', '../views/menu/systemuser/admin.vue', 10, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (12, '普通用户', '/systemUser/user', '../views/menu/systemuser/user.vue', 10, 'MENU', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (14, '个人资料', '/person', '../views/profile/Person.vue', -1, 'PROFILE', null, 1, null);
INSERT INTO temp.cp_menu (id, menu_name, path, component, parent_id, menu_type, permission, visible, icon) VALUES (15, '修改密码', '/password', '../views/profile/Password.vue', -1, 'PROFILE', null, 1, null);



create table cp_permission
(
    id          bigint unsigned auto_increment comment '权限ID'
        primary key,
    perm_name   varchar(64)                        not null comment '权限标识，唯一，如：user:create',
    description varchar(255)                       null comment '权限描述',
    created_at  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_perm_name
        unique (perm_name)
)
    comment '权限表';

INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (1, 'user:create', '创建用户', '2025-05-17 23:28:39', '2025-05-17 23:28:39');
INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (2, 'user:delete', '删除用户', '2025-05-17 23:28:39', '2025-05-17 23:28:39');
INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (3, 'user:update', '更新用户信息', '2025-05-17 23:28:39', '2025-05-17 23:28:39');
INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (4, 'user:view', '查看用户信息', '2025-05-17 23:28:39', '2025-05-17 23:28:39');
INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (5, 'role:create', '创建角色', '2025-05-17 23:28:39', '2025-05-17 23:28:39');
INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (6, 'role:update', '更新角色', '2025-05-17 23:28:39', '2025-05-17 23:28:39');
INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (7, 'role:delete', '删除角色', '2025-05-17 23:28:39', '2025-05-17 23:28:39');
INSERT INTO temp.cp_permission (id, perm_name, description, created_at, updated_at) VALUES (8, 'permission:assign', '分配权限', '2025-05-17 23:28:39', '2025-05-17 23:28:39');


create table cp_role
(
    id          bigint auto_increment comment '角色ID'
        primary key,
    role_name   varchar(50)                          not null comment '角色名称',
    description varchar(255)                         null comment '角色描述',
    created_at  timestamp  default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at  timestamp  default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted  tinyint(1) default 0                 null comment '逻辑删除标志 0-未删除 1-已删除',
    constraint role_name
        unique (role_name)
)
    comment '角色表';

create index idx_role_name
    on cp_role (role_name);

INSERT INTO temp.cp_role (id, role_name, description, created_at, updated_at, is_deleted) VALUES (1, 'ADMIN', '系统管理员', '2025-06-06 16:32:12', '2025-06-06 16:32:12', 0);
INSERT INTO temp.cp_role (id, role_name, description, created_at, updated_at, is_deleted) VALUES (2, 'MANAGER', '停车场管理员', '2025-06-06 16:32:12', '2025-06-06 16:32:12', 0);
INSERT INTO temp.cp_role (id, role_name, description, created_at, updated_at, is_deleted) VALUES (3, 'USER', '普通用户', '2025-06-06 16:32:12', '2025-06-06 16:32:12', 0);

create table cp_role_menu
(
    role_id     bigint                             not null comment '角色ID',
    menu_id     bigint                             not null comment '菜单ID',
    assigned_at datetime default CURRENT_TIMESTAMP null comment '创建时间',
    primary key (role_id, menu_id)
)
    comment '角色与菜单关联表';

INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 1, '2025-06-09 19:02:41');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 2, '2025-06-09 19:02:42');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 3, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 4, '2025-06-09 19:02:41');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 5, '2025-06-09 19:02:42');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 6, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 7, '2025-06-09 19:02:41');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 8, '2025-06-09 19:02:42');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 9, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 10, '2025-06-09 19:02:41');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 11, '2025-06-09 19:02:42');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 12, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 14, '2025-06-09 19:02:42');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (1, 15, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (3, 1, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (3, 3, '2025-06-09 19:02:41');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (3, 4, '2025-06-09 19:02:42');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (3, 5, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (3, 6, '2025-06-09 19:02:41');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (3, 14, '2025-06-09 19:02:43');
INSERT INTO temp.cp_role_menu (role_id, menu_id, assigned_at) VALUES (3, 15, '2025-06-09 19:02:41');


create table cp_role_permission
(
    role_id     bigint unsigned                    not null comment '角色ID，关联 sys_role.id',
    perm_id     bigint unsigned                    not null comment '权限ID，关联 sys_permission.id',
    assigned_at datetime default CURRENT_TIMESTAMP not null comment '分配时间',
    primary key (role_id, perm_id)
)
    comment '角色-权限关联表';

create index idx_perm_id
    on cp_role_permission (perm_id);

create index idx_role_id
    on cp_role_permission (role_id);

INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 1, '2025-05-17 23:28:52');
INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 2, '2025-05-17 23:28:52');
INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 3, '2025-05-17 23:28:52');
INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 4, '2025-05-17 23:28:52');
INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 5, '2025-05-17 23:28:52');
INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 6, '2025-05-17 23:28:52');
INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 7, '2025-05-17 23:28:52');
INSERT INTO temp.cp_role_permission (role_id, perm_id, assigned_at) VALUES (1, 8, '2025-05-17 23:28:52');



create table cp_user
(
    id         bigint auto_increment comment '用户ID'
        primary key,
    username   varchar(50)                             not null comment '用户名',
    password   varchar(255)                            not null comment '密码',
    nick_name  varchar(50)                             null comment '昵称',
    avatar     varchar(255)                            null comment '头像URL',
    email      varchar(100)                            null comment '邮箱',
    phone      varchar(20)                             null comment '手机号',
    sex        varchar(10)                             null comment '性别',
    created_at timestamp     default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at timestamp     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted tinyint(1)    default 0                 null comment '逻辑删除标志 0-未删除 1-已删除',
    account    double(10, 2) default 0.00              null comment '余额',
    constraint username
        unique (username)
)
    comment '用户表';

create index idx_email
    on cp_user (email);

create index idx_phone
    on cp_user (phone);

create index idx_username
    on cp_user (username);

INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (1, 'wdg', '$2a$10$OYGS4WPrAAKRtVUg0Gk1jOR2tDnsVCsAQnqT/fbajIipW1A1TF/.q', '系统管理1', null, 'admin@example.com', '15813699127', null, '2025-06-06 16:32:12', '2025-06-11 17:41:18', 0, 200);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (2, 'wdg411', '$2a$10$0cqoPCCrqsNVj9yzdI/aP.TxEm.airSwH1uDRqAw99.ftZS9ZtIom', 'wdg', null, 'hh@qq.com', '18124811625', '1', '2025-06-08 18:37:30', '2025-06-08 18:37:30', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (7, 'wdg412', '$2a$10$MQLV31yGG8Z1blRTiayDJulCbIhroeLrLQ7sOGmcOQgUsCqfJIOda', 'wdg', null, 'hh@qq.com', '18124811625', '1', '2025-06-08 18:42:44', '2025-06-08 18:42:44', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (9, 'wdg41152', '$2a$10$EXfqTjTiRslhfuPp1KzPh.pUDs/f/nZDy9AxD63kv0p.CcZD/2MQm', 'wdg', null, 'hh@qq.com', '18124811625', '1', '2025-06-08 18:50:37', '2025-06-08 18:50:37', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (10, 'wdg413', '$2a$10$E7KeLgFXz3GWu9sK1t4HFeqP7G/O8Av17XyvSkWajGMHtE3vE6mLq', 'wdg11', null, 'hh@qq.com', '18124811628', '1', '2025-06-08 18:50:49', '2025-06-11 17:52:52', 0, 800);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (11, 'wdg23', '$2a$10$SSGEgMU1LJPSDOHWBZIorecGsZoSLPHZi46.TkebVxipeIXDxiiJe', 'wdg', null, 'hh@qq.com', '18124811625', '1', '2025-06-08 18:54:34', '2025-06-08 18:54:34', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (12, 'fukk', '$2a$10$1GbAJRJ08AOR0pxz3aCyu.NlcIxKMr4Ju7etCj9JsZP3s8S9CGXnq', null, null, 'ax@qs.com', null, null, '2025-06-09 00:06:25', '2025-06-09 00:06:25', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (13, 'wdg111', '$2a$10$Fype1h1EsG.Rs4ddvkexAezFQ3pJ02hptgCeDj7tGhpGyI3fx/woK', null, null, '111@qq.com', null, null, '2025-06-09 15:49:07', '2025-06-09 15:49:07', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (14, 'wdwd', '$2a$10$SxaL930.XOdyp30Dm/LySu8THRLjUS.J4zT4ruopJQuhIsHE6QJqy', null, null, 're.life@icloud.com', null, null, '2025-06-09 15:51:26', '2025-06-09 15:51:26', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (15, 'www', '$2a$10$a8vP.ZN63scs8KV6zTZZDe72vMhMgEetlBwSTSjrPQrg89S2dGZBm', null, null, 're.life@icloud.com', null, null, '2025-06-09 15:52:06', '2025-06-09 15:52:06', 0, 0);
INSERT INTO temp.cp_user (id, username, password, nick_name, avatar, email, phone, sex, created_at, updated_at, is_deleted, account) VALUES (16, 'mrb', '$2a$10$hrg9Fg.WSVGUBSk5Ds0g0.JYHxaWybEeQnBo1hVQhOkLrz7fWrsge', null, null, 're.life@icloud.com', null, null, '2025-06-10 14:27:05', '2025-06-10 14:27:05', 0, 0);



create table cp_user_role
(
    user_id     bigint unsigned                    not null comment '用户ID，关联 sys_user.id',
    role_id     bigint unsigned                    not null comment '角色ID，关联 sys_role.id',
    assigned_at datetime default CURRENT_TIMESTAMP not null comment '分配时间',
    primary key (user_id, role_id)
)
    comment '用户-角色关联表';

create index idx_role_id
    on cp_user_role (role_id);

create index idx_user_id
    on cp_user_role (user_id);

INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (1, 1, '2025-06-07 16:19:06');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (2, 3, '2025-06-08 18:37:30');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (7, 3, '2025-06-08 18:42:48');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (9, 3, '2025-06-08 18:50:37');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (10, 3, '2025-06-08 18:50:49');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (11, 3, '2025-06-08 18:54:34');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (12, 3, '2025-06-09 00:06:25');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (13, 3, '2025-06-09 15:49:07');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (14, 3, '2025-06-09 15:51:26');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (15, 3, '2025-06-09 15:52:06');
INSERT INTO temp.cp_user_role (user_id, role_id, assigned_at) VALUES (16, 3, '2025-06-10 14:27:05');


create table location
(
    id    int auto_increment comment '主键ID'
        primary key,
    name  varchar(255)  null comment '区域名称',
    total int default 0 null comment '总车位数',
    num   int default 0 null comment '空闲车位'
)
    comment '区域信息表' collate = utf8mb4_unicode_ci
                         row_format = DYNAMIC;

INSERT INTO temp.location (id, name, total, num) VALUES (1, 'A区', 5, 5);
INSERT INTO temp.location (id, name, total, num) VALUES (2, 'B区', 8, 7);
INSERT INTO temp.location (id, name, total, num) VALUES (4, 'C区', 5, 5);



create table notice
(
    id      int auto_increment comment 'ID'
        primary key,
    title   varchar(255) null comment '标题',
    content varchar(255) null comment '内容',
    time    varchar(255) null comment '发布时间'
)
    comment '系统公告' collate = utf8mb4_unicode_ci
                       row_format = DYNAMIC;

INSERT INTO temp.notice (id, title, content, time) VALUES (1, '系统内侧', '系统功能已全部开发完成，开始进入测试阶段。', '2025-02-16 11:43:13');
INSERT INTO temp.notice (id, title, content, time) VALUES (2, '系统上线', '所有功能都已完成，可以正常上线，发布版本v1.0。', '2025-02-16 11:43:20');
INSERT INTO temp.notice (id, title, content, time) VALUES (3, '系统优化功能优化', '优化了系统的相关功能和用户操作体验。', '2025-02-16 11:43:18');


create table parking
(
    id             int auto_increment comment '主键ID'
        primary key,
    user_id        int          null comment '用户ID',
    vehicle_id     varchar(255) null comment '车辆ID',
    location_id    int          null comment '区域ID',
    parking_lot_id int          null comment '车位ID',
    start_time     varchar(255) null comment '入场时间',
    end_time       varchar(255) null comment '出场时间',
    status         varchar(255) null comment '状态'
)
    comment '停车信息' collate = utf8mb4_unicode_ci
                       row_format = DYNAMIC;

INSERT INTO temp.parking (id, user_id, vehicle_id, location_id, parking_lot_id, start_time, end_time, status) VALUES (7, 1, '3', 1, 1, '2025-02-14 08:00:00', '2025-02-14 11:00:00', '已出场');
INSERT INTO temp.parking (id, user_id, vehicle_id, location_id, parking_lot_id, start_time, end_time, status) VALUES (8, 2, '2', 2, 10, '2025-02-14 12:00:00', null, '已入场');


create table parking_lot
(
    id          int auto_increment comment '主键ID'
        primary key,
    name        varchar(255)                null comment '车位编号',
    location_id int                         null comment '区域ID',
    status      varchar(255) default '空闲' null comment '车位状态'
)
    comment '车位信息表' collate = utf8mb4_unicode_ci
                         row_format = DYNAMIC;

INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (1, 'A001', 1, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (2, 'A002', 1, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (3, 'A003', 1, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (6, 'A004', 1, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (7, 'A005', 1, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (8, 'B001', 2, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (9, 'B002', 2, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (10, 'B003', 2, '占用');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (11, 'B004', 2, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (12, 'B005', 2, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (13, 'B006', 2, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (14, 'B007', 2, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (15, 'B008', 2, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (16, 'C001', 4, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (17, 'C002', 4, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (18, 'C003', 4, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (19, 'C004', 4, '空闲');
INSERT INTO temp.parking_lot (id, name, location_id, status) VALUES (20, 'C005', 4, '空闲');



create table pay
(
    id             int auto_increment comment '主键ID'
        primary key,
    user_id        int           null comment '用户ID',
    vehicle_id     varchar(255)  null comment '车辆ID',
    location_id    int           null comment '区域ID',
    parking_lot_id int           null comment '车位ID',
    start_time     varchar(255)  null comment '入场时间',
    end_time       varchar(255)  null comment '出场时间',
    minutes        int           null comment '分钟',
    price          double(10, 2) null comment '金额',
    status         varchar(255)  null comment '缴费状态'
)
    comment '停车信息' collate = utf8mb4_unicode_ci
                       row_format = DYNAMIC;

INSERT INTO temp.pay (id, user_id, vehicle_id, location_id, parking_lot_id, start_time, end_time, minutes, price, status) VALUES (7, 1, '3', 1, 1, '2025-02-14 08:00:00', '2025-02-14 11:00:00', 180, 18, '已缴费');


create table vehicle
(
    id      int auto_increment comment '主键ID'
        primary key,
    name    varchar(255) null comment '车牌号',
    user_id int          null comment '用户ID'
)
    comment '车辆信息表' collate = utf8mb4_unicode_ci
                         row_format = DYNAMIC;

INSERT INTO temp.vehicle (id, name, user_id) VALUES (1, '皖A N999B', 1);
INSERT INTO temp.vehicle (id, name, user_id) VALUES (2, '皖A 888NB', 2);
INSERT INTO temp.vehicle (id, name, user_id) VALUES (3, '皖A 666NB', 1);
