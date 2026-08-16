# 在线教育系统

前后端分离的在线教育平台，基于尚硅谷「谷粒学院」，并扩展了文章系统与 Elasticsearch 检索。仓库内包含 **一个后端、两个前端**。

| 目录 | 角色 | 默认访问地址 |
| --- | --- | --- |
| `online-education-server` | Spring Cloud 微服务后端 | 网关 [http://localhost:8222](http://localhost:8222) |
| `online-education-web-admin` | 讲师 / 运营管理后台 | [http://localhost:9528](http://localhost:9528) |
| `online-education-web-front` | 学员前台网站 | [http://localhost:3000](http://localhost:3000) |

两个前端都通过网关 `8222` 访问后端，不要直连各个微服务端口。

建议启动顺序：中间件 → 后端微服务 → 两个前端。

---

## 管理后台 `online-education-web-admin`

讲师与运营使用的 Vue 后台，基于 [vue-admin-template](https://github.com/PanJiaChen/vue-admin-template) 二次开发。覆盖讲师、课程分类、课程发布、首页 Banner、订单、权限（用户 / 角色 / 菜单）、统计分析，以及文章管理。

开发接口指向网关：`http://localhost:8222`（见 `config/dev.env.js`）。

### 技术版本

| 项目 | 版本 |
| --- | --- |
| Node.js | 建议 **14.x**（`package.json` 声明 `>= 6`；`node-sass@4` 在 Node 10/12/14 最稳，不建议用 16+） |
| npm | `>= 3` |
| Vue | 2.5.17 |
| Vue Router | 3.0.1 |
| Vuex | 3.0.1 |
| Element UI | 2.4.6 |
| Axios | 0.18.0 |
| ECharts | 4.1.x |
| Webpack | 4.16.5 |
| vue-admin-template | 3.8.0 |

### 启动

```bash
cd online-education-web-admin
npm install
npm run dev
```

浏览器访问 [http://localhost:9528](http://localhost:9528)。

```bash
npm run build          # 生产打包，产物在 dist/
npm run build:report   # 打包并查看体积分析
```

---

## 用户前台 `online-education-web-front`

面向学员的 Nuxt SSR 站点（页面标题为「才华教育」）。支持课程浏览与播放、讲师介绍、登录注册、下单支付、文章浏览 / 写作 / 检索。

接口同样指向网关：`http://localhost:8222`（见 `utils/request.js`）。

### 技术版本

| 项目 | 版本 |
| --- | --- |
| Node.js | 建议 **16.x**（Nuxt 2.17 支持 14.18 / 16 / 18；`marked@10` 需要 Node 16+） |
| Nuxt | 2.17.0（`package.json` 写 `^2.0.0`，lock 锁定 2.17.0） |
| Vue | 2.7.14（由 Nuxt 引入） |
| Element UI | 2.15.5 |
| Axios | 0.19.2 |
| ECharts | 4.1.x |
| vue-awesome-swiper | 3.1.3 |
| marked | 10.x |
| highlight.js | 11.9.x |

与管理后台的 Node 大版本不同，不要用同一套 Node 硬跑两边。可用 nvm 切换，例如后台 `nvm use 12`，前台 `nvm use 16`。

### 启动

```bash
cd online-education-web-front
npm install
npm run dev
```

浏览器访问 [http://localhost:3000](http://localhost:3000)。

```bash
npm run build    # 生产构建
npm start        # 以 SSR 方式启动已构建产物
npm run generate # 导出静态站点
```

---

## 后端 `online-education-server`

Spring Cloud 微服务。在谷粒学院课程、点播、订单、权限等能力上，增加了文章服务，以及基于 Elasticsearch 的检索；可用 Canal 把 MySQL binlog 同步到 ES。

库表脚本：`online-education-server/online_education.sql`。

### 框架版本

| 项目 | 版本 |
| --- | --- |
| JDK | 1.8 |
| Maven | 3.x（工程含 `mvnw`） |
| Spring Boot | 2.2.1.RELEASE |
| Spring Cloud | Hoxton.RELEASE |
| Spring Cloud Alibaba | 0.2.2.RELEASE |
| Spring Cloud Gateway | 随 Hoxton（模块 `api_gateway`） |
| OpenFeign / Ribbon / Hystrix | 随 Hoxton |
| MyBatis-Plus | 3.0.5 |
| Springfox Swagger | 2.7.0 |
| JWT | 0.7.0 |
| Canal Client | 1.1.0 |
| 阿里云 OSS / VOD SDK | 见根 `pom.xml` |

### 中间件版本

以下为项目原部署环境中的版本；Redis / ES / Kibana 文档路径在 Linux `/opt/module`。

| 中间件 | 版本 | 地址 | 说明 |
| --- | --- | --- | --- |
| MySQL | **5.7.26** | `localhost:3306`，库名 `online_education` | 以 `online_education.sql` 导出信息为准 |
| Nacos | **1.1.x**（推荐 1.1.4） | `127.0.0.1:8848` | 与 Spring Cloud Alibaba 0.2.2 配套，只用服务发现 |
| Redis | **5.0.8** | 配置里为 `192.168.153.81:6379` | 登录态、短信验证码等 |
| Elasticsearch | **7.6.1** | `192.168.153.81:9200` | 文章检索 |
| Kibana | **7.6.1** | 随 ES 部署 | 可选，便于查看索引 |
| Canal | Client **1.1.0** | 模块 `canal_client`（端口 10001） | 可选，binlog → ES |

Redis、ES 的主机请改成自己的环境，不要沿用仓库里的内网 IP。账号密码在各模块 `application.properties` 中修改。

### 模块与端口

| 模块 | 端口 | 职责 |
| --- | --- | --- |
| `infrastructure/api_gateway` | 8222 | 网关，前端唯一入口 |
| `service/service_edu` | 8001 | 课程、讲师、文章等核心业务 |
| `service/service_oss` | 8002 | 阿里云 OSS 上传 |
| `service/service_vod` | 8003 | 阿里云视频点播 |
| `service/service_cms` | 8004 | 首页 Banner |
| `service/service_msm` | 8005 | 短信验证码 |
| `service/service_order` | 8007 | 订单与支付 |
| `service/service_statistics` | 8008 | 统计 |
| `service/service_acl` | 8009 | 后台权限 |
| `service/service_search` | 8010 | ES 检索 |
| `service/service_ucenter` | 8160 | 学员中心 |
| `canal_client` | 10001 | Canal 客户端（可选） |
| `common/*` | — | 公共工具、统一返回、Spring Security |

### 启动说明

**1. 准备中间件**

- 安装并启动 MySQL 5.7，创建库 `online_education`，导入 `online-education-server/online_education.sql`。
- 启动 Nacos（控制台一般为 [http://127.0.0.1:8848/nacos](http://127.0.0.1:8848/nacos)）。
- 启动 Redis、Elasticsearch；需要看索引时再开 Kibana（部署在Linux中）。

Linux 上原启动命令示例：

```bash
# Redis
cd /opt/module/redis-5.0.8/
redis-server redis.conf

# Elasticsearch
cd /opt/module/elasticsearch-7.6.1/bin/
./elasticsearch

# Kibana（可选）
cd /opt/module/kibana-7.6.1/bin
./kibana
```

Nacos 在 Windows 本机启动即可，默认注册地址已是 `127.0.0.1:8848`。

**2. 修改配置**

各服务 `src/main/resources/application.properties` 中把 MySQL、Redis、ES 的地址和账号改成当前环境。OSS / 点播 / 短信还需要填阿里云密钥。

**3. 编译并启动微服务**

```bash
cd online-education-server
mvn clean install -DskipTests
```

在 IDE 中分别运行各模块的启动类，或对模块执行 `mvn spring-boot:run`。建议顺序：

1. `api_gateway`
2. `service_edu`、`service_ucenter`、`service_acl`、`service_cms` 等业务服务
3. 需要检索时再启动 `service_search`；需要 binlog 同步时再启动 Canal Server 与 `canal_client`

Nacos 控制台能看到对应服务名（如 `service-edu`、`service-gateway`）即注册成功。

**4. 再开前端**

网关 `8222` 起来之后，再启动管理后台和用户前台。
