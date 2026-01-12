# Red Culture Backend

红色文化资源管理平台后端服务系统，提供完整的红色文化资源管理、用户认证、权限控制等功能。

## 技术栈

| 技术 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 3.2.2 |
| Spring Security | - |
| MyBatis-Plus | 3.5.5 |
| MySQL | 8.0 |
| JWT | - |
| Swagger/OpenAPI | 3.0 |
| Lombok | - |
| Maven | - |

## 项目结构

```
red-culture-backend/
├── src/main/java/com/redculture/backend/
│   ├── RedCultureBackendApplication.java    # 主启动类
│   ├── config/                              # 配置类
│   ├── controller/                          # 控制器层
│   ├── dto/                                 # 数据传输对象
│   ├── entity/                              # 实体类
│   ├── mapper/                              # MyBatis接口
│   ├── security/                            # 安全配置
│   └── service/                             # 服务层
├── src/main/resources/
│   ├── application.yml                      # 主配置文件
│   └── mapper/                              # MyBatis XML映射
└── pom.xml                                  # Maven配置
```

## 功能模块

### 用户认证与授权
- JWT令牌认证机制
- 基于角色的访问控制(RBAC)
- 密码BCrypt加密存储
- 支持四种角色：ADMIN、EDITOR、USER、VISITOR

### 红色景点管理
- 景点信息的增删改查
- 地理位置坐标记录
- 保护等级管理
- 历史背景描述

### 历史人物管理
- 人物详细信息管理
- 与景点的关联关系

### 历史事件管理
- 事件记录存储
- 时间线管理

### 教育资源管理
- 教育资源分类存储
- 资源访问权限控制

### 组织管理
- 红色组织信息管理
- 组织层级关系

### 参观活动管理
- 活动安排记录
- 参与者统计

### 访客反馈管理
- 访客意见收集
- 反馈统计分析

### 统计分析
- 数据可视化接口
- 访问量统计
- 热门景点排行

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA 推荐)

### 安装步骤

1. 克隆项目
```bash
git clone <repository-url>
cd red-culture-backend
```

2. 配置数据库
```bash
# 创建数据库
CREATE DATABASE red_culture_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库脚本
mysql -u root -p red_culture_db < src/main/resources/update_schema.sql
```

3. 配置环境变量
```bash
# 复制环境变量示例文件
cp .env.example .env

# 编辑 .env 文件，填写数据库密码和JWT密钥
```

4. 修改 application.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/red_culture_db
    username: root
    password: your_database_password

app:
  jwtSecret: ${JWT_SECRET}
  jwtExpirationMs: 86400000
```

5. 编译运行
```bash
mvn clean install
mvn spring-boot:run
```

6. 访问API文档
```
http://localhost:8080/swagger-ui.html
```

## 默认账户

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | password123 |

## API接口说明

### 认证接口
- POST /api/auth/login - 用户登录
- POST /api/auth/register - 用户注册

### 红色景点
- GET /api/red-spots - 获取景点列表
- GET /api/red-spots/{id} - 获取景点详情
- POST /api/red-spots - 创建景点(需认证)
- PUT /api/red-spots/{id} - 更新景点(需认证)
- DELETE /api/red-spots/{id} - 删除景点(需认证)

### 历史人物
- GET /api/figures - 获取人物列表
- GET /api/figures/{id} - 获取人物详情
- POST /api/figures - 创建人物(需认证)
- PUT /api/figures/{id} - 更新人物(需认证)
- DELETE /api/figures/{id} - 删除人物(需认证)

### 历史事件
- GET /api/events - 获取事件列表
- GET /api/events/{id} - 获取事件详情
- POST /api/events - 创建事件(需认证)
- PUT /api/events/{id} - 更新事件(需认证)
- DELETE /api/events/{id} - 删除事件(需认证)

### 统计分析
- GET /api/statistics/dashboard - 获取统计数据
- GET /api/statistics/popular-spots - 获取热门景点

## 权限说明

| 角色 | 权限 |
|------|------|
| ADMIN | 所有权限 |
| EDITOR | 可编辑内容，无法管理用户 |
| USER | 可查看内容，可提交反馈 |
| VISITOR | 仅可查看公开内容 |

## 配置说明

### JWT配置
JWT密钥建议使用Base64编码，至少256位：
```bash
echo -n "your-secret-key-here" | base64
```

### 跨域配置
默认允许所有来源访问，生产环境请修改 `CorsConfig.java`。

## 数据库表结构

- red_spot - 红色景点
- historical_figure - 历史人物
- historical_event - 历史事件
- education_resource - 教育资源
- organization - 组织信息
- visit_activity - 参观活动
- visitor_feedback - 访客反馈
- sys_user - 系统用户
- sys_role - 系统角色
- sys_user_role - 用户角色关联
- spot_figure_relation - 景点人物关联

## 开发指南

### 代码规范
- 遵循阿里巴巴Java开发规范
- 使用Lombok简化代码
- 统一使用ApiResponse返回格式

### 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试相关
chore: 构建/工具变动
```

## 许可证

MIT License

## 联系方式

如有问题，请提交 Issue 或 Pull Request。