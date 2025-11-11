FROM gradle:jdk21-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build --no-daemon -x test
LABEL org.name="testk"

FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /home/gradle/src/build/libs/kit-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]