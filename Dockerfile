FROM navikt/java:11
COPY target/pam-xml-stilling-app-*.jar app.jar
EXPOSE 9020
