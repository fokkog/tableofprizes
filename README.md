# Table Of Prizes

![](https://github.com/fokkog/tableofprizes/workflows/CI/badge.svg)

## intro

Once ready, this application intends to support a **virtual table of prizes**.

Festive events like bridge (the card game) drives traditionally present a physical table of prizes. These days all bridge has moved to the Internet and so should the rewards, however after a quick scan I could not find an existing website for this. Hence I registered [www.prijzentafel.nl](https://www.prijzentafel.nl/) and [www.tableofprizes.com](https://www.tableofprizes.com/) and initiated this project. Note: at the moment these domains are unresponsive as I found the Azure running costs too steep, see also below.

Of course the real goal is to learn stuff, mainly:

- Spring Boot, to practice my old Java skills while using C# in my day job
- OpenID Connect (OIDC) and its support in Azure
- GitHub, specifically GitHub Actions for CI

The application is public by nature, but it will have to identify table managers and prize winners. For this I would prefer to rely on existing social accounts like Google/Microsoft. Hence my interest in OIDC, specifically as supported by Azure B2C Tenants.

To bootstrap the new project I could have used [Spring Initializr](https://start.spring.io/), but I decided to go one step further and use [JHipster](https://www.jhipster.tech/). Not sure how hip this really is nowadays, but it gives me a solid and secure foundation consisting of an Angular front-end and a Spring Boot back-end. See [.yo-rc.json](.yo-rc.json) for initial choices.

This project will consume some of my spare time, maybe for the coming hours/maybe for the coming years. It's about the journey, not the destination. By nature I am a back-end developer, so I would be very surprised if it ends in something good-looking and user-friendly :smiley:

## development

Prerequisites for running locally:

- JDK 11
- Node.js 12

For development I use Visual Studio Code with a few extensions, Language Support for Java being the main one.
Running locally requires opening 2 terminal windows to start the following processes:

1. `mvn` (to build and run back-end and front-end on port 8080)
2. `npm start` (to build and run additional front-end on port 9000)

## deployment

This application will be deployed to the Azure cloud, specifically to an Azure App Service. See the [GitHub pipeline](.github/workflows/ci.yml) for CI details. The maven step builds a 'fat' JAR file that is typical of Spring boot. A dedicated deploy step (note: no use of the [Maven Plugin for Azure App Service](https://github.com/Microsoft/azure-maven-plugins/tree/master/azure-webapp-maven-plugin)) copies that JAR file to the App Service. A permanent/manually created web.config file takes care of the App Service integration.

web.config contents:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <system.webServer>
    <handlers>
      <add name="httpPlatformHandler" path="*" verb="*" modules="httpPlatformHandler" resourceType="Unspecified" />
    </handlers>
    <httpPlatform processPath="%JAVA_HOME%\bin\java.exe"
      arguments="-Djava.net.preferIPv4Stack=true -Dserver.port=%HTTP_PLATFORM_PORT% -jar &quot;%HOME%\site\wwwroot\app.jar&quot;"
      stdoutLogEnabled="true" startupRetryCount="1" startupTimeLimit="600" requestTimeout="00:02:00" />
  </system.webServer>
</configuration>
```

Differences between running locally and running in the Azure cloud:

1. The DB provider changes from H2 (in-memory) to Azure SQL Database (SQL Server in the cloud, the cheapest DB option available in Azure).
2. Log levels become lower.

## lessons learned so far

1. Azure (at least a Windows-based App Service) is hardly fit to run Spring Boot applications. I am running on a **B1** App Service Plan that costs a hefty 46.17 EUR per month. This is the 'cheapest' plan that supports custom domains and SSL. The initial version of this application takes 20 seconds to start locally and 10 minutes or more (!) to start in the Azure cloud.
2. I got off on the wrong foot: AAD B2C is not a transparent variation on AAD. The user flow concept adds a layer of flexibility/complexity, hence AAB B2C has its own starter kit and its own configuration.
