# MinMVP - 技术栈全覆盖演示平台

## 项目简介

MinMVP 是一个单页面 MVP 演示系统，通过一个页面集成展示**用户管理、数据统计、全文搜索、消息通知、定时任务、文件管理、Excel导出、API文档、权限控制、富文本编辑、PDF预览**等核心功能，体现完整的技术栈能力。

## 技术栈

### 前端技术栈

| 技术 | 版本 | 用途 |
|---|---|---|
| Vue.js | 3.x | 核心框架 |
| Element Plus | 最新 | UI 组件库 |
| Pinia | 最新 | 状态管理（用户Token、数据、消息） |
| Vue Router | 4.x | 单页面路由 |
| ECharts | 5.x | 数据统计图表（柱状图/饼图） |
| Lodash | 4.x | JavaScript 工具类 |
| Day.js | 1.x | 日期时间处理 |
| Less | 3.x | CSS 预处理器 |
| Vite | 7.x | 构建工具 |

### 后端技术栈（设计规范）

| 技术 | 版本 | 用途 |
|---|---|---|
| Spring Boot | 2.0.9.RELEASE | 后端应用框架 |
| MyBatis-Plus | 3.1.2 | ORM（分页/CRUD） |
| MySQL | 5.7 | 主数据库 |
| Druid | 1.2.6 | 连接池 |
| Redis | - | 用户信息缓存 |
| Elasticsearch | 7.3.2 | 全文检索 |
| Kafka | - | 操作日志消息队列 |
| XXL-Job | 2.0.1 | 定时任务 |
| FastDFS/OSS | - | 文件存储 |
| EasyExcel | 2.1.6 | Excel 导入导出 |
| Swagger/Knife4j | 2.9.2 | API 文档 |
| java-jwt | 3.8.1 | JWT 认证 |
| MapStruct | 1.4.1.Final | DTO/Entity 转换 |
| HSAF | 1.2.7 | 统一认证/权限控制 |
| Hutool | 5.0.6 | 工具类 |
| FastJSON | 1.2.83 | JSON 处理 |
| Jasypt | - | 配置加密 |

## 功能模块

1. **用户管理** - 分页列表、新增/编辑/删除、Redis缓存（MyBatis-Plus + Redis）
2. **数据统计** - 柱状图（用户增长趋势）+ 饼图（角色分布）（ECharts + XXL-Job）
3. **全文搜索** - ES索引搜索，支持姓名/手机号模糊搜索（Elasticsearch 7.3.2）
4. **消息通知** - 实时操作日志流，模拟Kafka消息推送（Kafka + Pinia）
5. **定时任务** - 任务状态展示、手动触发（XXL-Job 2.0.1）
6. **文件管理** - 文件上传/下载/预览（FastDFS/OSS）
7. **Excel导入导出** - 用户数据CSV导出、模拟导入（EasyExcel 2.1.6）
8. **API文档** - Swagger风格接口文档展示（Swagger 2.9.2 + Knife4j）
9. **权限控制** - RBAC角色权限矩阵、JWT Token信息（HSAF 1.2.7）
10. **富文本编辑** - HTML富文本编辑+预览（CKEditor集成）
11. **PDF预览** - PDF在线翻页预览（vue-pdf 4.3.0）

## 快速开始

```bash
# 安装依赖
npm install

# 开发模式
npm run dev

# 生产构建
npm run build

# 预览构建产物
npm run preview
```

## 推荐 IDE

[VS Code](https://code.visualstudio.com/) + [Vue (Official)](https://marketplace.visualstudio.com/items?itemName=Vue.volar)
