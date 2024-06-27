package lu.uni.binfocep.javaee.exercise3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Covid19DataService {

    // Properties
    private static final Logger logger = Logger.getLogger(Covid19DataService.class.getName());
    private static final String COVID_API_URL = "https://disease.sh/v3/covid-19/countries";
    private List<Covid19Data> cachedData = new ArrayList<>();

    // Methods to fetch data for Vaadin
    @PostConstruct
    @Scheduled(fixedRate = 3600000) // Fetch and cache data every hour
    public void fetchAndCacheData() throws IOException  {
        logger.info("Starting `fetchAndCacheData` method");
        try {
            logger.info("Fetching data from API: " + COVID_API_URL);
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(COVID_API_URL, String.class);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            Covid19Data[] data = mapper.readValue(response, Covid19Data[].class);
            if (data != null) {
                cachedData = List.of(data);
                logger.info("Fetched and cached data for " + cachedData.size() + " countries");
                //logCovid19Data(cachedData);
            } else {
                logger.warning("No data fetched from API. Response: " + response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching data from API", e);
        }
    }

    // Method to fetch data for GraphiQL
    @Cacheable("covid19Data")
    public List<Map<String, Object>> fetchAllData() {
        logger.info("Starting `fetchAllData` method");
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            logger.info("Fetching data from API: " + COVID_API_URL);
            String jsonResponse = restTemplate.getForObject(COVID_API_URL, String.class);
            return objectMapper.readValue(jsonResponse, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse COVID-19 data", e);
        }
    }


    // Method to return COVID19 data based on cached data initially fetched
    public List<Covid19Data> getCovid19DataLazyByCountry(int offset, int limit, List<String> countries, List<String> selectedFields) {
        List<Covid19Data> result = cachedData.stream()
                .filter(data -> countries.contains(data.getCountry()))
                .map(data -> filterFields(data, selectedFields))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        
        logger.info("Returning data for " + result.size() + " countries after filtering");

        //logCovid19Data(result);

        return result;
    }
    
    // Method to return COVID19 data size based on cache
    public int getCovid19DataLazyByCountrySize(List<String> countries) {
        return (int) cachedData.stream()
                .filter(data -> countries.contains(data.getCountry()))
                .count();
    }

    // Method to return COVID19 data in cache filtered by selected fields
    private Covid19Data filterFields(Covid19Data data, List<String> selectedFields) {
        Covid19Data filteredData = new Covid19Data();
        filteredData.setCountry(data.getCountry());
        filteredData.setCases(data.getCases());
        filteredData.setDeaths(data.getDeaths());

        if (selectedFields.contains("updated")) {
            filteredData.setUpdated(data.getUpdated());
        }
        if (selectedFields.contains("countryInfo")) {
            filteredData.setCountryInfo(data.getCountryInfo());
        }
        if (selectedFields.contains("todayCases")) {
            filteredData.setTodayCases(data.getTodayCases());
        }
        if (selectedFields.contains("todayDeaths")) {
            filteredData.setTodayDeaths(data.getTodayDeaths());
        }
        if (selectedFields.contains("recovered")) {
            filteredData.setRecovered(data.getRecovered());
        }
        if (selectedFields.contains("todayRecovered")) {
            filteredData.setTodayRecovered(data.getTodayRecovered());
        }
        if (selectedFields.contains("active")) {
            filteredData.setActive(data.getActive());
        }
        if (selectedFields.contains("critical")) {
            filteredData.setCritical(data.getCritical());
        }
        if (selectedFields.contains("casesPerOneMillion")) {
            filteredData.setCasesPerOneMillion(data.getCasesPerOneMillion());
        }
        if (selectedFields.contains("deathsPerOneMillion")) {
            filteredData.setDeathsPerOneMillion(data.getDeathsPerOneMillion());
        }
        if (selectedFields.contains("tests")) {
            filteredData.setTests(data.getTests());
        }
        if (selectedFields.contains("testsPerOneMillion")) {
            filteredData.setTestsPerOneMillion(data.getTestsPerOneMillion());
        }
        if (selectedFields.contains("population")) {
            filteredData.setPopulation(data.getPopulation());
        }
        if (selectedFields.contains("continent")) {
            filteredData.setContinent(data.getContinent());
        }
        if (selectedFields.contains("oneCasePerPeople")) {
            filteredData.setOneCasePerPeople(data.getOneCasePerPeople());
        }
        if (selectedFields.contains("oneDeathPerPeople")) {
            filteredData.setOneDeathPerPeople(data.getOneDeathPerPeople());
        }
        if (selectedFields.contains("oneTestPerPeople")) {
            filteredData.setOneTestPerPeople(data.getOneTestPerPeople());
        }
        if (selectedFields.contains("activePerOneMillion")) {
            filteredData.setActivePerOneMillion(data.getActivePerOneMillion());
        }
        if (selectedFields.contains("recoveredPerOneMillion")) {
            filteredData.setRecoveredPerOneMillion(data.getRecoveredPerOneMillion());
        }
        if (selectedFields.contains("criticalPerOneMillion")) {
            filteredData.setCriticalPerOneMillion(data.getCriticalPerOneMillion());
        }

        logger.fine("Filtered data for country: " + filteredData.getCountry());
        return filteredData;
    }

    // Method to list all countries from COVID19 data in cache
    public List<String> getAvailableCountries() {
        logger.info("Starting `getAvailableCountries` method");
        List<String> countries = cachedData.stream()
                .map(Covid19Data::getCountry)
                .collect(Collectors.toList());
        logger.info("Returning list of " + countries.size() + " available countries");
        return countries;
    }


    // For loggin purposes:
    private void logCovid19Data(List<Covid19Data> dataList) {
        for (Covid19Data data : dataList) {
            logger.info("Covid19Data:");
            logger.info("  Updated: " + data.getUpdated());
            logger.info("  Country: " + data.getCountry());
            logger.info("  Cases: " + data.getCases());
            logger.info("  Today Cases: " + data.getTodayCases());
            logger.info("  Deaths: " + data.getDeaths());
            logger.info("  Today Deaths: " + data.getTodayDeaths());
            logger.info("  Recovered: " + data.getRecovered());
            logger.info("  Today Recovered: " + data.getTodayRecovered());
            logger.info("  Active: " + data.getActive());
            logger.info("  Critical: " + data.getCritical());
            logger.info("  Cases Per One Million: " + data.getCasesPerOneMillion());
            logger.info("  Deaths Per One Million: " + data.getDeathsPerOneMillion());
            logger.info("  Tests: " + data.getTests());
            logger.info("  Tests Per One Million: " + data.getTestsPerOneMillion());
            logger.info("  Population: " + data.getPopulation());
            logger.info("  Continent: " + data.getContinent());
            logger.info("  One Case Per People: " + data.getOneCasePerPeople());
            logger.info("  One Death Per People: " + data.getOneDeathPerPeople());
            logger.info("  One Test Per People: " + data.getOneTestPerPeople());
            logger.info("  Active Per One Million: " + data.getActivePerOneMillion());
            logger.info("  Recovered Per One Million: " + data.getRecoveredPerOneMillion());
            logger.info("  Critical Per One Million: " + data.getCriticalPerOneMillion());

            CountryInfo countryInfo = data.getCountryInfo();
            if (countryInfo != null) {
                logger.info("  CountryInfo:");
                logger.info("    ID: " + countryInfo.getId());
                logger.info("    ISO2: " + countryInfo.getIso2());
                logger.info("    ISO3: " + countryInfo.getIso3());
                logger.info("    Lat: " + countryInfo.getLat());
                logger.info("    Long: " + countryInfo.getLongg());
                logger.info("    Flag: " + countryInfo.getFlag());
            } else {
                logger.warning("  CountryInfo is null");
            }
        }
    }
}
