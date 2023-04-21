ARG jre="eclipse-temurin:17-jdk-jammy"

FROM $jre as builder
WORKDIR application
ARG JAR_FILE=assets/usr/kuehnenagel/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM $jre
WORKDIR usr/kuehnenagel/
EXPOSE 8000
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
RUN true
COPY assets/etc/ /etc/
COPY --from=builder application/application/ ./
ENV JDK_JAVA_OPTIONS="-Xms256m -Xmx512m -server"
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher", "--spring.config.location=/etc/kuehnenagel/config/application.properties"]
