FROM openjdk:8-jre-alpine

WORKDIR /app

COPY docker/entrypoint.sh ./
COPY target/blogapi-fat.jar ./

ENV DATABASE_USER=root
ENV DATABASE_PASSWORD=blogapi
ENV DATABASE_URL=jdbc:mysql://dbserver:3306/blogapi?characterEncoding=UTF-8&useSSL=false

EXPOSE 8080
ENTRYPOINT ["./entrypoint.sh"]
