FROM openjdk:8

# 安装MySQL客户端
#RUN apt-get update && apt-get install -y mysql-client

# 安装MySQL服务
RUN apt-get update && apt-get install -y mysql-server

# 复制您的Java应用程序和MySQL配置文件
COPY target/web_content_translate-0.0.1-SNAPSHOT.jar /app/web_content_translate.jar
#COPY my.cnf /etc/mysql/my.cnf

# 创建数据卷挂载点
VOLUME /var/lib/mysql

# 设置MySQL root用户的密码
ENV MYSQL_ROOT_PASSWORD 123456

# 暴露8080端口
EXPOSE 8080

# 暴露MySQL默认端口3306
EXPOSE 3306

# 启动MySQL服务并运行您的Java应用程序
ENTRYPOINT service mysql start && java -jar /app/web_content_translate.jar