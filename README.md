# Real Estate Valuation

Application developed in JavaFx for the real estate valuation in the Spanish housing sector through the data of the Idealista web portal.

## Getting Started

The project uses Maven, therefore only a couple of commands will be necessary for the installation and execution of the application.

### Prerequisites

For installation and execution it is necessary to have installed a JDK 8 or higher and Maven 3.

```
Maven 3.5 (older versions might work too)
Java Developer Kit 8 with at least Update 40
```

### Installing

Once the repository is cloned, execute the following command commands in the project directory

```
mvn clean install
mvn jfx:run
```


## Running

### Prerequisites

When the application is executed the first time it's necessary configure the Preferences through File menu.
It is necessary to have access to the Idealist API in addition to the Google Geocoding API. You can obtain an API Idealista access in the follow link: http://developers.idealista.com/access-request
```
Idealista ApiKey (Idealist API)
Idealista Secret (Idealist API)
Google ApiKey    (Google Geocoding API)
```
Note: If these values are not available yet, you can test the Application by setting "TEST" as a value in the three previous configuration fields. When this configuration is established the application will obtain the data of a preloaded Postman Mock.

The params Private and Professional factor could be set to 1 for the first tests.

## Deployment

For the generation of the installer will be necessary the dependencies used by the plugin "JavaFX Maven Plugin"

```
https://github.com/javafx-maven-plugin/javafx-maven-plugin
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Carlos Sánchez** - *Initial work* - [Carlos Sa Ma](https://github.com/carsanch)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
