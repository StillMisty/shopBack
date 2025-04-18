FROM openjdk:21 AS build

WORKDIR /workspace/app

# 复制Maven配置和源代码
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# 确保mvnw有执行权限
RUN chmod +x ./mvnw
# 构建应用
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# 运行阶段
FROM openjdk:21
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# 复制依赖
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# 入口点
ENTRYPOINT ["java","-cp","app:app/lib/*","top.stillmisty.shopback.ShopBackApplication"]

# 暴露端口
EXPOSE 8080