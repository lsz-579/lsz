-- 图书管理系统数据库初始化脚本

CREATE DATABASE IF NOT EXISTS tushu DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE tushu;

-- 图书表
CREATE TABLE IF NOT EXISTS book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '书名',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    isbn VARCHAR(20) UNIQUE COMMENT 'ISBN',
    category VARCHAR(50) COMMENT '分类',
    publisher VARCHAR(100) COMMENT '出版社',
    publish_date DATE COMMENT '出版日期',
    description VARCHAR(500) COMMENT '简介',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB COMMENT='图书表';

-- 借阅记录表
CREATE TABLE IF NOT EXISTS borrow_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL COMMENT '图书ID',
    borrower_name VARCHAR(50) NOT NULL COMMENT '借阅人',
    borrow_date DATE NOT NULL COMMENT '借阅日期',
    return_date DATE COMMENT '归还日期',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-借出, 1-已归还',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (book_id) REFERENCES book(id)
) ENGINE=InnoDB COMMENT='借阅记录表';

-- 初始测试数据
INSERT INTO book (title, author, isbn, category, publisher, publish_date, description) VALUES
('平凡的世界', '路遥', '9787530212004', '文学', '北京十月文艺出版社', '2012-03-01', '以中国70年代中期到80年代中期为背景，刻画了当时社会各阶层众多普通人的形象。'),
('活着', '余华', '9787530215531', '文学', '北京十月文艺出版社', '2017-06-01', '讲述了农村人福贵悲惨的人生遭遇。'),
('三体', '刘慈欣', '9787536692930', '科幻', '重庆出版社', '2008-01-01', '讲述了地球人类文明和三体文明的信息交流、生死搏杀及两个文明在宇宙中的兴衰历程。'),
('百年孤独', '加西亚·马尔克斯', '9787544253994', '文学', '南海出版公司', '2011-06-01', '描写了布恩迪亚家族七代人的传奇故事，以及加勒比海沿岸小镇马孔多的百年兴衰。'),
('深入理解Java虚拟机', '周志明', '9787111421900', '技术', '机械工业出版社', '2013-06-01', '以Java虚拟机为核心，系统讲解JVM工作原理和优化实践。');

INSERT INTO borrow_record (book_id, borrower_name, borrow_date, return_date, status) VALUES
(1, '张三', '2024-01-15', '2024-02-15', 1),
(1, '李四', '2024-03-01', NULL, 0),
(2, '王五', '2024-01-20', '2024-02-20', 1),
(2, '张三', '2024-04-10', NULL, 0),
(3, '李四', '2024-02-01', '2024-03-01', 1),
(4, '赵六', '2024-05-01', NULL, 0),
(5, '王五', '2024-03-15', '2024-04-15', 1);
