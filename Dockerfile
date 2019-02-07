FROM navikt/java:11
COPY target/pam-xml-stilling-app-*-jar-with-dependencies.jar /app/app.jar
EXPOSE 9020
