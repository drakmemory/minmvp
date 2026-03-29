-- ============================================================
-- MinMVP Database Schema for MySQL 5.7
-- ============================================================

-- User table
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(20),
  `email` varchar(100),
  `role` varchar(20) NOT NULL DEFAULT 'user',
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `avatar` varchar(255),
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- File table
CREATE TABLE IF NOT EXISTS `sys_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `original_name` varchar(255) NOT NULL,
  `size` bigint(20) NOT NULL DEFAULT 0,
  `type` varchar(20),
  `path` varchar(500) NOT NULL,
  `url` varchar(500),
  `upload_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `uploader_id` bigint(20),
  `uploader_name` varchar(50),
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Message/log table
CREATE TABLE IF NOT EXISTS `sys_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `user_name` varchar(50),
  `content` varchar(500) NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) NOT NULL DEFAULT 'success',
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Task table
CREATE TABLE IF NOT EXISTS `sys_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `cron` varchar(50) NOT NULL,
  `handler` varchar(100),
  `last_run` datetime,
  `next_run` datetime,
  `status` varchar(20) NOT NULL DEFAULT 'success',
  `duration` varchar(20),
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Rich text table
CREATE TABLE IF NOT EXISTS `sys_rich_text` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200),
  `content` text,
  `creator_id` bigint(20),
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Permission table
CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(20) NOT NULL,
  `module` varchar(50) NOT NULL,
  `can_view` tinyint(1) NOT NULL DEFAULT 0,
  `can_create` tinyint(1) NOT NULL DEFAULT 0,
  `can_edit` tinyint(1) NOT NULL DEFAULT 0,
  `can_delete` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Default Data
-- ============================================================

-- Default users (password is BCrypt hash of "123456")
INSERT INTO `sys_user` (`username`, `password`, `name`, `phone`, `email`, `role`, `status`) VALUES
  ('admin',     '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张三',  '13800000001', 'admin@minmvp.com',     'admin',   1),
  ('zhangwei',  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张伟',  '13800000002', 'zhangwei@minmvp.com',  'manager', 1),
  ('lina',      '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李娜',  '13800000003', 'lina@minmvp.com',      'user',    1),
  ('wangqiang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王强',  '13800000004', 'wangqiang@minmvp.com', 'user',    1),
  ('zhaolei',   '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '赵磊',  '13800000005', 'zhaolei@minmvp.com',   'user',    1),
  ('sunmin',    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '孙敏',  '13800000006', 'sunmin@minmvp.com',    'user',    1);

-- Default tasks
INSERT INTO `sys_task` (`name`, `cron`, `handler`, `last_run`, `next_run`, `status`, `duration`) VALUES
  ('每日用户统计', '0 0 2 * * ?',   'userStatJobHandler',  '2024-01-01 02:00:00', '2024-01-02 02:00:00', 'success', '2.3s'),
  ('ES数据同步',   '0 0/30 * * * ?', 'esSyncJobHandler',   '2024-01-01 10:30:00', '2024-01-01 11:00:00', 'success', '5.1s'),
  ('缓存清理',     '0 0 3 * * ?',   'cacheClearJobHandler','2024-01-01 03:00:00', '2024-01-02 03:00:00', 'success', '1.0s'),
  ('数据库备份',   '0 0 4 * * ?',   'dbBackupJobHandler',  '2024-01-01 04:00:00', '2024-01-02 04:00:00', 'success', '15.6s');

-- Permission matrix: admin has full access to all modules
INSERT INTO `sys_permission` (`role`, `module`, `can_view`, `can_create`, `can_edit`, `can_delete`) VALUES
  ('admin', 'user',      1, 1, 1, 1),
  ('admin', 'file',      1, 1, 1, 1),
  ('admin', 'message',   1, 1, 1, 1),
  ('admin', 'task',      1, 1, 1, 1),
  ('admin', 'richtext',  1, 1, 1, 1),
  ('admin', 'permission',1, 1, 1, 1);

-- Permission matrix: manager has most access except permission management
INSERT INTO `sys_permission` (`role`, `module`, `can_view`, `can_create`, `can_edit`, `can_delete`) VALUES
  ('manager', 'user',      1, 1, 1, 0),
  ('manager', 'file',      1, 1, 1, 1),
  ('manager', 'message',   1, 1, 1, 0),
  ('manager', 'task',      1, 0, 0, 0),
  ('manager', 'richtext',  1, 1, 1, 1),
  ('manager', 'permission',1, 0, 0, 0);

-- Permission matrix: user has limited access
INSERT INTO `sys_permission` (`role`, `module`, `can_view`, `can_create`, `can_edit`, `can_delete`) VALUES
  ('user', 'user',      1, 0, 0, 0),
  ('user', 'file',      1, 1, 0, 0),
  ('user', 'message',   1, 0, 0, 0),
  ('user', 'task',      1, 0, 0, 0),
  ('user', 'richtext',  1, 1, 1, 0),
  ('user', 'permission',0, 0, 0, 0);

-- Sample messages
INSERT INTO `sys_message` (`type`, `user_name`, `content`, `time`, `status`) VALUES
  ('login',  '张三', '管理员登录系统',             '2024-01-01 09:00:00', 'success'),
  ('export', '张伟', '导出用户数据报表',           '2024-01-01 10:15:00', 'success'),
  ('upload', '李娜', '上传文件: 项目需求文档.docx', '2024-01-01 11:30:00', 'success'),
  ('edit',   '王强', '修改个人信息',               '2024-01-01 14:00:00', 'success'),
  ('login',  '赵磊', '用户登录系统',               '2024-01-01 15:20:00', 'success'),
  ('delete', '张三', '删除过期文件',               '2024-01-01 16:45:00', 'warning');
