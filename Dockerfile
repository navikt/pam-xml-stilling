FROM navikt/java:12
COPY target/pam-xml-stilling-*-jar-with-dependencies.jar /app/app.jar
EXPOSE 9020
