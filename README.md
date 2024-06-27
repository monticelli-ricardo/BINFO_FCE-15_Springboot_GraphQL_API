# Exercise3 GraphQL API running in Spring Boot

This project implements a GraphQL API using Spring Boot to retrieve COVID-19 data from an external source (https://disease.sh/v3/covid-19/countries) and expose it via a GraphiQL interface. 

There is an additional web interface based on Vaadin accessible at: `http://localhost:8080/exercise3/view/`

## Getting Started

To run this application locally, follow these steps:

1. Unzip the project:

   ```bash
   unzip BINFO_FCE-15_Exercise3_Springboot_GraphQL_API-main.zip
   cd exercise3/
   ```
   

2. Build the application using Gradle:

   ```bash
   ./gradlew build
   ```

3. Run the application:

   ```bash
   ./gradlew bootRun
   ```

   The application will start at `http://localhost:8080`.

4. Open your web browser and navigate to `http://localhost:8080/exercise3` to access the GraphiQL interface.

## Available Queries

### Get COVID-19 Data based on provided country(s) names

Retrieve COVID-19 data for specific countries' names and selected subfields.

```graphql
{
  getCovid19DataByCountry(countries: ["Afghanistan", "USA"]) {
    country
    cases
    deaths
    recovered
    population
  }
}
```

### Get COVID-19 Data

Retrieve available COVID-19 data based on selected subfields.

```graphql
{
  getCovid19Data {
    country
    cases
    deaths
    recovered
    population
  }
}
```
