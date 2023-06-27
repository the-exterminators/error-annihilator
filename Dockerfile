# Stage that builds the application, a prerequisite for the running stage
FROM maven:3.8.1-openjdk-17-slim as build
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

# install git and ssh client on image
RUN apt-get update && apt-get install -y openssh-client git

#RUN apt-get update && \
#    apt-get install --yes --no-install-recommends \
#    openssh-client \
#    git \
#    && apt-get clean && \

# Stop running as root at this point
RUN useradd -m myuser
WORKDIR /usr/src/app/
RUN chown myuser:myuser /usr/src/app/
USER myuser

# Copy pom.xml and prefetch dependencies so a repeated build can continue from the next step with existing dependencies
COPY --chown=myuser pom.xml ./
RUN mvn dependency:go-offline -Pproduction

# Copy all needed project files to a folder
COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser frontend frontend

# add vaadin server-key
ARG offlinekey
ENV VAADIN_OFFLINE_KEY=$offlinekey

# Build the production package, assuming that we validated the version before so no need for running tests again.
RUN mvn clean package -Pproduction -Dvaadin.productionMode -Dvaadin.offline -Dvaadin.offline.key=eyJraWQiOiI1NDI3NjRlNzAwMDkwOGU2NWRjM2ZjMWRhYmY0ZTJjZDI4OTY2NzU4IiwidHlwIjoiSldUIiwiYWxnIjoiRVM1MTIifQ.eyJhbGxvd2VkUHJvZHVjdHMiOlsidmFhZGluLWNvbGxhYm9yYXRpb24tZW5naW5lIiwidmFhZGluLXRlc3RiZW5jaCIsInZhYWRpbi1kZXNpZ25lciIsInZhYWRpbi1jaGFydCIsInZhYWRpbi1ib2FyZCIsInZhYWRpbi1jb25maXJtLWRpYWxvZyIsInZhYWRpbi1jb29raWUtY29uc2VudCIsInZhYWRpbi1yaWNoLXRleHQtZWRpdG9yIiwidmFhZGluLWdyaWQtcHJvIiwidmFhZGluLW1hcCIsInZhYWRpbi1zcHJlYWRzaGVldC1mbG93IiwidmFhZGluLWNydWQiXSwic3ViIjoiMWVmYjZkNzQtZWI0OC00OTRkLWE5N2EtYWY5ZDA2OTM4NWI0IiwidmVyIjoxLCJpc3MiOiJWYWFkaW4iLCJuYW1lIjoiU3RlZmFuIEVsIEFiZGVsbGFvdWkiLCJhbGxvd2VkRmVhdHVyZXMiOlsiY2VydGlmaWNhdGlvbnMiLCJzcHJlYWRzaGVldCIsInRlc3RiZW5jaCIsImRlc2lnbmVyIiwiY2hhcnRzIiwiYm9hcmQiLCJhcHBzdGFydGVyIiwidmlkZW90cmFpbmluZyIsInByby1wcm9kdWN0cy0yMDIyMTAiXSwibWFjaGluZV9pZCI6bnVsbCwic3Vic2NyaXB0aW9uIjoiVmFhZGluIFBybyIsImJ1aWxkX3R5cGVzIjpbInByb2R1Y3Rpb24iXSwiZXhwIjoxNzE4OTI4MDAwLCJpYXQiOjE2ODc3ODgzODksImFjY291bnQiOiJNYW5hZ2VtZW50IENlbnRlciBJbm5zYnJ1Y2sifQ.AHQRyxUNG6g59lXR-upHtUjJjI1K3mhGiJQk79Sbq3Ia988pAbFEmRqwR_Xbn_xMEnPy3v7mWPl-yii8SEPcadWFAPKcP2cTOxldLknoPNubkMw70wNh7ElFbJhA7hSqTkRgVvq61fR-HLdUXOoJFj0oVqgL73XdLDXJrwe8xiJ64KNk

# Running stage: the part that is used for running the application
FROM maven:3.8.1-openjdk-17-slim
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD java -jar /usr/app/app.jar
