FROM maven:3.8-openjdk-11 as builder

WORKDIR /daoyun
COPY . .
RUN mvn clean install


FROM openjdk:11

WORKDIR /daoyun/bin
COPY --from=builder /daoyun/target/*.jar .

EXPOSE 8099
CMD [ "sh", "-c", "java -jar $(find /daoyun/bin -name \"daoyun*.jar\")" ]

