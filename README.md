# Introduction 
This repository contains the implementation of the claim compliance provider. For a detailed view on the architecture visit /docs/ directory. 

## Functional
The claim compliance provider is a service that initiates the signing of claims, checks the compliance of signed claims with the Gaia-X Trust Framework and interacts with a catalogue implementation such as the [XFSC federated catalogue](https://gitlab.eclipse.org/eclipse/xfsc/cat) which is a default implementation.

## Technical
### Overview
The service is implemented as a [quarkus application](https://quarkus.io/) with a REST API. 

The app receives claims and participant credentials as input and returns a list of three verifiable presentations containing signed `ServiceOffering`(s), related `Resource`s and the compliance status of the claims to be submitted to a catalogue implementation.

Both payload and response are in JSON format. 

### References
* See [OpenAPI documentation](https://claim-compliance-provider.gxfs.gx4fm.org/docs/) for more details on the API.
  * One endpoint for submitting claims and receiving verifiable presentations.
  * One endpoint for creating a set of new claims from three input parameters.
  * One endpoint which exposes configurations, especially the URLs of the 3rd party services.
* See [docs](docs) folder in this repository for architectural insights. 
* See [this reopository](https://github.com/GAIA-X4PLC-AAD/gaia-x-compliant-claims-example) for an example of creating compliant claims.

# Getting Started
Follow these steps to get the project up and running on your local machine:

## 1. Configure and build the Project

Use Maven to build the project. Run the following command in your terminal:

```bash
mvn clean install
```

## 2. Build the Docker Image
Build the Docker image using the provided Dockerfile. Run the following command in your terminal:
```bash
docker build -t ccp -f claim-compliance-provider-controller/src/main/docker/Dockerfile.jvm ./claim-compliance-provider-controller
```

## 3. Run the Docker Container
Start the Docker container with port mapping. Add the environment variables listed below and run the following command in your terminal:
```bash
docker run -i --rm -p 8080:8080 ccp -e <environment-variable>=<value>
```
| Environment Variable | Value                                 | Example                                       |
| -------------------- |---------------------------------------|-----------------------------------------------|
| `SD_CREATOR_URL`     | URL for the (default) SdCreatorClient | `https://sd-creator.gxfs.gx4fm.org`           |
| `COMPLIANCE_SERVICE_URL` | URL for the ComplianceServiceClient   | `https://compliance.lab.gaia-x.eu/v1-staging` |
| `FEDERATED_CATALOGUE_URL` | URL for the FederatedCatalogueClient  | `https://fc-server.gxfs.gx4fm.org`            |
| `KEYCLOAK_URL` | URL for the Keycloak server           | `https://fc-keycloak.gxfs.gx4fm.org`                  |
| `KEYCLOAK_SECRET` | Client secret for the Keycloak client | `my-secret-key`                               |
| `KEYCLOAK_USERNAME` | Username for the Keycloak client      | `my-username`                                 |
| `KEYCLOAK_PASSWORD` | Password for the Keycloak client      | `my-password`                                 |


## 4. API References
API documentation is available at the [docs](https://claim-compliance-provider.gxfs.gx4fm.org/docs/) endpoint.

## 5. Software Dependencies
see [dependencies](claim-compliance-provider-controller/target/quarkus-app/quarkus-app-dependencies.txt) after building the project.

## 6. Latest Releases
TODO: No releases yet. Add information about the latest releases here.

# Contribute
Feel free to contribute to this project.
You can do this by pushing a new branch and creating a pull request or by a fork of this repository with a pull request.

# License
see [LICENSE](LICENSE).
