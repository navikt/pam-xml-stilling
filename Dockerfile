FROM navikt/java:11
COPY app/target/pam-ad-api-app-*.jar /app/app.jar
EXPOSE 9014
