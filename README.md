# 网上商城的后端实现

# 1. 项目介绍

## 核心技术栈

* Java 21：编程语言
* Spring Boot：Web应用框架
* Spring Data JPA：ORM框架，处理数据持久化
* Spring Security：安全认证和授权
* PostgreSQL：数据库

## 辅助工具和库

* JWT (io.jsonwebtoken)：用于身份验证和令牌管理
* Swagger/OpenAPI：API文档生成和可视化（springdoc-openapi）
* Lombok：简化Java代码开发
* Hutool：工具类库
* H2 Database：用于测试环境，但最后也没写单元测试

## 构建和工具

* Maven：项目构建和依赖管理
* Hibernate：作为JPA的实现
* Spring Boot DevTools：开发工具支持
* Bean Validation：数据验证

## 其他特性

* 使用了GraalVM Native Image支持，试了一下午，问题太多，最后放弃了，Java对Native Image的支持还是不够友好。
* 集成了Spring REST Docs文档生成
* 项目遵循RESTful API设计风格

# 2. 配置

## 数据库配置

在`src/main/resources/application.properties`中配置数据库连接信息，这是一定要按照你自己的数据库配置来修改的

``` text
# PostgreSQL 数据库配置，shop是数据库名称
spring.datasource.url=jdbc:postgresql://localhost:5432/shop
# 数据库用户名
spring.datasource.username=postgres
# 数据库用户名对应密码
spring.datasource.password=2004
```

## JWT配置

在`src/main/resources/application.properties`中配置JWT密钥和过期时间，一般你用默认的就行了

``` text
# JWT 密钥
jwt.secret=your_secret_key
# JWT 过期时间（单位：毫秒）
jwt.expiration=86400000
```

## Swagger配置

Swagger UI 提供了一个用户友好的界面，允许你查看API的所有端点、请求参数和响应格式。

在`src/main/resources/application.properties`中配置Swagger的基本信息，用默认的就好了，要不你还要改Spring security的配置

启动项目后 Swagger 访问路径 http://127.0.0.1:8080/swagger-ui/index.html

``` text
# 是否启用Swagger,
springdoc.swagger-ui.enabled=true
```

## 添加管理员需要的密钥

在`src/main/resources/application.properties`中配置添加管理员需要的密钥，默认是123456，你可以改成其他的，将用户注册成管理员时需要用到

``` text
admin.secret=123456
```

# 3. 启动项目

## 3.1 源代码启动

IDEA中直接运行`src/main/java/top/stillmisty/shopback/ShopBackApplication.java`即可。

## 3.2 Jar包启动

``` shell
# 进入Jar包所在目录
cd /path/to/jar
# 启动Jar包，Jar名要对上，其会创建一个src目录以存储图片等数据
java -jar shopBack-0.0.1.jar
```

## 3.3 Docker启动

休息会再搞，欢迎PR

# 4. 其他

因为我就学了一周不到的Spring Boot，翻看源代码时或许会看到很割裂的地方，或者不太符合设计原则的地方，欢迎PR