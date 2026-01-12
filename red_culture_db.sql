-- 数据库创建
CREATE DATABASE IF NOT EXISTS red_culture_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE red_culture_db;

-- ==========================================
-- 1. 系统权限模块 (System Auth)
-- ==========================================

-- 1.1 系统用户表 (Sys User)
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '加密密码',
    full_name VARCHAR(100) COMMENT '全名',
    email VARCHAR(100) UNIQUE COMMENT '邮箱',
    status INT DEFAULT 1 COMMENT '状态(1:启用, 0:禁用)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 1.2 系统角色表 (Sys Role)
CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称(如: ROLE_ADMIN)',
    description VARCHAR(200) COMMENT '描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 1.3 用户角色关联表 (Sys User Role)
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';


-- ==========================================
-- 2. 核心业务模块 (Core Business)
-- ==========================================

-- 2.1 红色景点表 (Red Spot)
CREATE TABLE red_spot (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '景点ID',
    name VARCHAR(100) NOT NULL COMMENT '景点名称',
    address VARCHAR(255) COMMENT '地理位置',
    protection_level VARCHAR(50) COMMENT '保护级别 (如: 国家级, 省级)',
    history_background TEXT COMMENT '历史背景',
    longitude DECIMAL(10, 7) COMMENT '经度',
    latitude DECIMAL(10, 7) COMMENT '纬度',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='红色景点信息表';

-- 2.2 历史人物表 (Historical Figure)
CREATE TABLE historical_figure (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '人物ID',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    birth_date DATE COMMENT '出生日期',
    death_date DATE COMMENT '逝世日期',
    biography TEXT COMMENT '生平事迹',
    hometown VARCHAR(100) COMMENT '籍贯'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='历史人物信息表';

-- 2.3 参观单位表 (Organization)
CREATE TABLE organization (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '单位ID',
    name VARCHAR(150) NOT NULL COMMENT '单位名称',
    contact_person VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    org_type VARCHAR(50) COMMENT '单位类型 (学校, 机关, 企业等)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参观单位信息表';

-- 2.4 历史事件表 (Historical Event)
CREATE TABLE historical_event (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '事件ID',
    title VARCHAR(150) NOT NULL COMMENT '事件标题',
    event_date DATE COMMENT '发生时间',
    description TEXT COMMENT '事件描述',
    red_spot_id INT COMMENT '关联红色景点ID',
    FOREIGN KEY (red_spot_id) REFERENCES red_spot(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='历史事件信息表';

-- 2.5 景点与人物关联表 (Spot-Figure Relation)
CREATE TABLE spot_figure_relation (
    red_spot_id INT NOT NULL,
    historical_figure_id INT NOT NULL,
    PRIMARY KEY (red_spot_id, historical_figure_id),
    FOREIGN KEY (red_spot_id) REFERENCES red_spot(id) ON DELETE CASCADE,
    FOREIGN KEY (historical_figure_id) REFERENCES historical_figure(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点与人物关联表';

-- 2.6 教育资源表 (Education Resource)
CREATE TABLE education_resource (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '资源ID',
    title VARCHAR(200) NOT NULL COMMENT '资源标题',
    resource_type ENUM('Book', 'Article', 'Image', 'Video', 'Audio') NOT NULL COMMENT '资源类型',
    content_url VARCHAR(500) COMMENT '资源链接或存储路径',
    description TEXT COMMENT '资源简介',
    publish_date DATE COMMENT '发布/出版日期',
    red_spot_id INT COMMENT '关联红色景点ID',
    FOREIGN KEY (red_spot_id) REFERENCES red_spot(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教育资源信息表';

-- 2.7 参观活动表 (Visit Activity)
CREATE TABLE visit_activity (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '活动ID',
    theme VARCHAR(200) NOT NULL COMMENT '活动主题',
    visit_time DATETIME NOT NULL COMMENT '参观时间',
    participant_count INT DEFAULT 0 COMMENT '参与人数',
    status ENUM('Pending', 'Approved', 'Rejected', 'Reserved', 'Completed', 'Cancelled') DEFAULT 'Pending' COMMENT '状态',
    organization_id INT COMMENT '组织单位ID',
    red_spot_id INT COMMENT '参观景点ID',
    user_id BIGINT COMMENT '申请用户ID',
    FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE,
    FOREIGN KEY (red_spot_id) REFERENCES red_spot(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参观学习活动表';

-- 2.8 用户反馈表 (Visitor Feedback)
CREATE TABLE visitor_feedback (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '反馈ID',
    visitor_name VARCHAR(50) DEFAULT 'Anonymous' COMMENT '参观者姓名',
    content TEXT NOT NULL COMMENT '反馈内容/心得',
    rating INT CHECK (rating BETWEEN 1 AND 5) COMMENT '评分(1-5)',
    feedback_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    visit_activity_id INT COMMENT '关联参观活动ID',
    FOREIGN KEY (visit_activity_id) REFERENCES visit_activity(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户反馈评价表';


-- ==========================================
-- 3. 数据初始化 (Data Initialization)
-- ==========================================

-- 3.1 初始化角色
INSERT INTO sys_role (name, description) VALUES 
('ROLE_ADMIN', '系统管理员，拥有所有权限'),
('ROLE_EDITOR', '内容编辑，负责内容管理'),
('ROLE_USER', '普通注册用户'),
('ROLE_VISITOR', '访客，仅查看权限');

-- 3.2 初始化用户 (默认管理员 admin / password123)
-- BCrypt: $2a$10$YR8IfV0ED/YRc17iIdQUAOUh173i/Te6.ls3JLvW1a5IgatOFgbn. matches 'password123'
INSERT INTO sys_user (username, password, full_name, email, status) VALUES 
('admin', '$2a$10$YR8IfV0ED/YRc17iIdQUAOUh173i/Te6.ls3JLvW1a5IgatOFgbn.', 'System Administrator', 'admin@redculture.com', 1);

-- 3.3 关联管理员角色
INSERT INTO sys_user_role (user_id, role_id) 
SELECT u.id, r.id 
FROM sys_user u, sys_role r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- 3.4 红色景点数据
INSERT INTO red_spot (name, address, protection_level, history_background, longitude, latitude) VALUES
('黄麻起义和鄂豫皖苏区纪念园', '湖北省红安县城关镇', '国家级', '纪念黄麻起义和鄂豫皖苏区革命根据地斗争历史', 114.6234, 31.2891),
('李先念故居纪念园', '湖北省红安县高桥镇', '国家级', '李先念主席诞生和早期生活战斗的地方', 114.5512, 31.1234),
('七里坪长胜街', '湖北省红安县七里坪镇', '国家级', '保留完好的革命旧址群', 114.7890, 31.4567),
('董必武故居', '湖北省红安县', '省级', '董必武同志的出生地', 114.6666, 31.3333),
('红四方面军诞生地', '湖北省红安县七里坪', '国家级', '中国工农红军第四方面军成立地', 114.7800, 31.4500),
('鄂豫皖特区苏维埃政府旧址', '湖北省红安县', '省级', '苏维埃政权建设见证', 114.6200, 31.2900),
('红安烈士陵园', '湖北省红安县', '国家级', '安葬着众多革命烈士', 114.6300, 31.2800),
('抗日军政大学第十分校旧址', '湖北省红安县', '省级', '培养抗日干部的学校', 114.5000, 31.1000),
('高桥革命旧址', '湖北省红安县高桥', '市级', '早期农运中心', 114.5600, 31.1300),
('秦基伟故居', '湖北省红安县', '省级', '秦基伟将军故居', 114.7000, 31.4000);

-- 3.5 历史人物数据
INSERT INTO historical_figure (name, birth_date, death_date, biography, hometown) VALUES
('李先念', '1909-06-23', '1992-06-21', '伟大的无产阶级革命家、政治家、军事家', '湖北红安'),
('董必武', '1886-03-05', '1975-04-02', '中国共产党的创始人之一', '湖北红安'),
('秦基伟', '1914-11-16', '1997-02-02', '中国人民解放军高级将领', '湖北红安'),
('王近山', '1915-10-29', '1978-05-10', '著名抗日将领，亮剑李云龙原型之一', '湖北红安'),
('陈锡联', '1915-01-04', '1999-06-10', '开国上将', '湖北红安'),
('韩先楚', '1913-01-30', '1986-10-03', '开国上将，以勇猛著称', '湖北红安'),
('郑位三', '1902-10-21', '1975-07-27', '鄂豫皖根据地主要创始人之一', '湖北红安'),
('吴石', '1894-08-01', '1950-06-10', '中共地下党员，牺牲于台湾', '福建闽侯'),
('徐海东', '1900-06-17', '1970-03-25', '开国大将', '湖北大悟'),
('刘华清', '1916-10-01', '2011-01-14', '中国现代海军之父', '湖北大悟');

-- 3.6 历史事件数据
INSERT INTO historical_event (title, event_date, description, red_spot_id) VALUES
('黄麻起义', '1927-11-13', '中国共产党在湖北黄安、麻城地区领导的农民起义', 1),
('红四方面军成立', '1931-11-07', '在七里坪成立，徐向前任总指挥', 5),
('鄂豫皖苏区反围剿', '1930-11-01', '红军在鄂豫皖苏区进行的多次反围剿作战', 1),
('七里坪战役', '1932-08-01', '红军与国民党军队在七里坪的激战', 3),
('李先念回乡', '1980-05-01', '李先念主席回到阔别多年的故乡视察', 2),
('董必武参加中共一大', '1921-07-23', '董必武作为代表出席中国共产党第一次全国代表大会', 4),
('建立鄂豫皖特区苏维埃', '1930-06-01', '苏维埃政权正式建立', 6),
('抗大十分校开学', '1940-03-01', '抗日军政大学第十分校举行开学典礼', 8),
('红安解放', '1949-04-01', '红安县城获得解放', 1),
('秦基伟率部抗战', '1937-10-01', '秦基伟在太行山区进行抗日斗争', 10);

-- 3.7 景点与人物关联
INSERT INTO spot_figure_relation (red_spot_id, historical_figure_id)
SELECT s.id, f.id
FROM red_spot s, historical_figure f
WHERE (s.name = '李先念故居纪念园' AND f.name = '李先念')
   OR (s.name = '董必武故居' AND f.name = '董必武')
   OR (s.name = '秦基伟故居' AND f.name = '秦基伟')
   OR (s.name = '黄麻起义和鄂豫皖苏区纪念园' AND f.name = '李先念')
   OR (s.name = '黄麻起义和鄂豫皖苏区纪念园' AND f.name = '董必武')
   OR (s.name = '黄麻起义和鄂豫皖苏区纪念园' AND f.name = '秦基伟')
   OR (s.name = '黄麻起义和鄂豫皖苏区纪念园' AND f.name = '王近山')
   OR (s.name = '红四方面军诞生地' AND f.name = '徐海东')
   OR (s.name = '七里坪长胜街' AND f.name = '徐海东')
   OR (s.name = '抗日军政大学第十分校旧址' AND f.name = '李先念');

-- 3.8 教育资源数据
INSERT INTO education_resource (title, resource_type, content_url, description, publish_date, red_spot_id) VALUES
('黄麻起义史料汇编', 'Book', '/lib/books/hmqy.pdf', '详细记录黄麻起义经过的书籍', '1990-01-01', 1),
('李先念传', 'Book', '/lib/books/lxn.pdf', '李先念主席的传记', '1995-05-01', 2),
('红安将军志', 'Book', '/lib/books/generals.pdf', '介绍红安籍将军的图书', '2000-10-01', 1),
('七里坪风云', 'Video', '/lib/videos/qlp.mp4', '反映七里坪革命斗争的纪录片', '2010-07-01', 3),
('董必武家书', 'Article', '/lib/articles/dbw_letters.html', '董必武写给家人的书信', '1985-03-01', 4),
('红军歌谣选', 'Audio', '/lib/audio/songs.mp3', '流传在鄂豫皖苏区的红军歌谣', '2005-06-01', 1),
('鄂豫皖苏区地图集', 'Image', '/lib/images/map.jpg', '当年苏区形势图', '2008-01-01', 6),
('秦基伟日记选', 'Article', '/lib/articles/qjw_diary.html', '秦基伟将军的战地日记', '1998-08-01', 10),
('吴石将军生平展', 'Image', '/lib/images/wushi.jpg', '吴石将军的生平图片展资料', '2025-10-27', NULL),
('抗大校歌', 'Audio', '/lib/audio/kangda.mp3', '抗日军政大学校歌录音', '1940-01-01', 8);

-- 3.9 参观单位数据
INSERT INTO organization (name, contact_person, phone, org_type) VALUES
('红安县第一中学', '张老师', '13800138001', 'School'),
('武汉大学历史学院', '李教授', '027-87654321', 'School'),
('湖北省某某机关党支部', '王书记', '13900139000', 'Government'),
('红安城投公司', '刘经理', '0713-5241000', 'Company'),
('实验小学', '赵老师', '13700137000', 'School'),
('华中科技大学团委', '孙老师', '027-87541111', 'School'),
('某某部队三连', '周指导员', '18900189000', 'Military'),
('夕阳红老年大学', '吴校长', '13600136000', 'Other'),
('红色文化研究会', '郑会长', '13500135000', 'Other'),
('某某旅行社', '钱导游', '13300133000', 'Company');

-- 3.10 参观活动数据
INSERT INTO visit_activity (theme, visit_time, participant_count, status, organization_id, red_spot_id, user_id) VALUES
('缅怀革命先烈', '2023-04-05 09:00:00', 50, 'Completed', 1, 7, NULL),
('大学生暑期社会实践', '2023-07-15 10:00:00', 30, 'Completed', 2, 1, NULL),
('七一建党节主题党日', '2023-07-01 09:30:00', 20, 'Completed', 3, 1, NULL),
('企业团建红色之旅', '2023-10-01 08:00:00', 40, 'Completed', 4, 3, NULL),
('少先队员入队仪式', '2023-10-13 14:00:00', 100, 'Completed', 5, 2, NULL),
('青年马克思主义者培养工程', '2023-11-04 09:00:00', 60, 'Reserved', 6, 5, NULL),
('部队传统教育', '2023-08-01 08:00:00', 80, 'Completed', 7, 1, NULL),
('重走长征路', '2023-05-04 09:00:00', 25, 'Cancelled', 9, 3, NULL),
('老年红歌会', '2023-09-09 15:00:00', 45, 'Completed', 8, 2, NULL),
('周末亲子游', '2023-11-11 10:00:00', 15, 'Reserved', 10, 4, NULL);

-- 3.11 用户反馈数据
INSERT INTO visitor_feedback (visitor_name, content, rating, visit_activity_id) VALUES
('张三', '非常有教育意义，深受感动。', 5, 1),
('李四', '讲解员讲得很生动，了解了很多历史。', 5, 2),
('王五', '设施维护得很好，环境庄严肃穆。', 4, 3),
('赵六', '行程安排紧凑，希望能多留点时间自由参观。', 3, 4),
('孙七', '孩子们受了很大教育，很有意义。', 5, 5),
('匿名', '希望能增加一些互动体验项目。', 4, 2),
('周八', '交通稍微有点不便，但值得一来。', 4, 1),
('吴九', '资料很丰富，不虚此行。', 5, 7),
('郑十', '活动组织有序，服务周到。', 5, 9),
('匿名', '洗手间有点少。', 3, 3);

-- ==========================================
-- 4. 常用查询示例
-- ==========================================

-- 1. 查询“黄麻起义和鄂豫皖苏区纪念园”的所有历史事件及其关联人物
SELECT e.title AS EventTitle, e.event_date, f.name AS FigureName
FROM historical_event e
JOIN red_spot s ON e.red_spot_id = s.id
LEFT JOIN spot_figure_relation sfr ON s.id = sfr.red_spot_id
LEFT JOIN historical_figure f ON sfr.historical_figure_id = f.id
WHERE s.name = '黄麻起义和鄂豫皖苏区纪念园';

-- 2. 统计2023年7月每个红色景点的参观团队数量，按数量降序排列
SELECT s.name AS SpotName, COUNT(v.id) AS VisitCount
FROM red_spot s
JOIN visit_activity v ON s.id = v.red_spot_id
WHERE v.visit_time BETWEEN '2023-07-01' AND '2023-07-31'
GROUP BY s.id, s.name
ORDER BY VisitCount DESC;

-- 3. 查询评分在5分的反馈详情，显示活动主题、单位名称和反馈内容
SELECT va.theme, o.name AS UnitName, vf.visitor_name, vf.content
FROM visitor_feedback vf
JOIN visit_activity va ON vf.visit_activity_id = va.id
JOIN organization o ON va.organization_id = o.id
WHERE vf.rating = 5;

-- 4. 查找“李先念”相关的所有教育资源（书籍、视频等）
SELECT r.title, r.resource_type, r.description
FROM education_resource r
LEFT JOIN red_spot s ON r.red_spot_id = s.id
LEFT JOIN spot_figure_relation sfr ON s.id = sfr.red_spot_id
LEFT JOIN historical_figure f ON sfr.historical_figure_id = f.id
WHERE f.name = '李先念' OR r.title LIKE '%李先念%';

-- 5. 查询所有“国家级”红色景点中，举办过“学校”类型单位参观活动的景点名称和活动次数
SELECT s.name AS SpotName, COUNT(v.id) AS SchoolVisitCount
FROM red_spot s
JOIN visit_activity v ON s.id = v.red_spot_id
JOIN organization o ON v.organization_id = o.id
WHERE s.protection_level = '国家级' AND o.org_type = 'School'
GROUP BY s.id, s.name;
