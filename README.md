# Exercise3 GraphQL API running in Spring Boot

This project implements a GraphQL API using Spring Boot to retrieve COVID-19 data from an external source (https://disease.sh/v3/covid-19/countries) and expose it via a GraphiQL interface. 

There is an additional web interface built on Vaadin, it is accessible at: `http://localhost:8080/exercise3/view/`

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
  getCovid19DataByCountry(countries:["Venezuela","Luxembourg"]) {
    continent
    country
    countryInfo {
      lat
      long
      _id
    }
    cases
    deaths
    recovered
  }
}
```

This is also possible on the Vaadin web interface (`http://localhost:8080/exercise3/view/`), through the button `Fetch By Countries`. 
If you desire to get additional information apart from the default fields ("number of deaths", "number of cases") select additional fields available in the `Checkbox group` before clicking the button.

### Get COVID-19 Data

Retrieve available COVID-19 data based on selected subfields.

```graphql
{
  getCovid19Data{
    continent
    country
    cases
    deaths
    recovered
  }
}
```

This is also possible on the Vaadin web interface (`http://localhost:8080/exercise3/view/`), through the button `Fetch Data`.
If you desire to get additional information apart from the default fields ("number of deaths", "number of cases") select additional fields available in the `Checkbox group` before clicking the button.
