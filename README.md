# tableofprizes

Once ready, this application intends to support a **virtual table of prizes**.

Festive events like bridge (the card game) drives traditionally present a physical table of prizes. These days all bridge has moved to the Internet, hence so should the table of prizes. However after a quick scan I could not find an existing website for this. Hence I registered [www.prijzentafel.nl](https://www.prijzentafel.nl/) and [www.tableofprizes.com](https://www.tableofprizes.com/) and initiated this project.

Of course the real goal is to learn stuff, mainly:

- Java/Spring Boot, to practice my old skills while using C#/.NET in my day job
- OpenID Connect (OIDC) and its support in Azure
- GitHub, specifically GitHub Actions for CI

To bootstrap the new project I could have used [Spring Initializr](https://start.spring.io/), but I decided to go one step further and use [JHipster](https://www.jhipster.tech/). Not sure how hip this really is nowadays, but it gives me an Angular SPA front-end and a Spring Boot back-end to get going. See [.yo-rc.json](.yo-rc.json) for initial choices.

This project will consume some of my spare time, maybe for the coming hours/maybe for the coming years. It's about the journey, not the destination. By nature I am a back-end developer, so I would be very surprised if it ends in something good-looking and user-friendly :smiley:

## development

Prerequisites for running locally:

- JDK
- Node.js

For development I use Visual Studio Code with a few extensions, Language Support for Java being the main one.

Running the generated application locally requires opening 3 consoles to start the following processes:

1. `./mvnw`
2. `npm start`
3. `docker-compose -f src/main/docker/keycloak.yml up`

## deployment

This application will be deployed to the Azure cloud, initially to an Azure App Service. See the GitHub pipeline for details.

Differences between running locally and running in the Azure cloud:

1. The DB provider changes from H2 (in-memory) to Azure SQL Database (SQL Server in the cloud, the cheapest DB option available in Azure).
2. The OIDC provider changes from Keycloak to Azure Active Directory B2C.
