package lu.uni.binfocep.javaee.exercise3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Covid19Data {

    // Properties
    @JsonProperty("updated")
    private long updated;

    @JsonProperty("country")
    private String country;

    @JsonProperty("countryInfo")
    private CountryInfo countryInfo;

    @JsonProperty("cases")
    private int cases;

    @JsonProperty("todayCases")
    private int todayCases;

    @JsonProperty("deaths")
    private int deaths;

    @JsonProperty("todayDeaths")
    private int todayDeaths;

    @JsonProperty("recovered")
    private int recovered;

    @JsonProperty("todayRecovered")
    private int todayRecovered;

    @JsonProperty("active")
    private int active;

    @JsonProperty("critical")
    private int critical;

    @JsonProperty("casesPerOneMillion")
    private float casesPerOneMillion;

    @JsonProperty("deathsPerOneMillion")
    private float deathsPerOneMillion;

    @JsonProperty("tests")
    private int tests;

    @JsonProperty("testsPerOneMillion")
    private float testsPerOneMillion;

    @JsonProperty("population")
    private int population;

    @JsonProperty("continent")
    private String continent;

    @JsonProperty("oneCasePerPeople")
    private int oneCasePerPeople;

    @JsonProperty("oneDeathPerPeople")
    private int oneDeathPerPeople;

    @JsonProperty("oneTestPerPeople")
    private int oneTestPerPeople;

    @JsonProperty("activePerOneMillion")
    private float activePerOneMillion;

    @JsonProperty("recoveredPerOneMillion")
    private float recoveredPerOneMillion;

    @JsonProperty("criticalPerOneMillion")
    private float criticalPerOneMillion;

    // Getters and Setters
    
    public Long getUpdated() {
        return updated;
    }
    public void setUpdated(Long updated) {
        this.updated = updated;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public CountryInfo getCountryInfo() {
        return countryInfo;
    }
    public void setCountryInfo(CountryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }
    public int getCases() {
        return cases;
    }
    public void setCases(int cases) {
        this.cases = cases;
    }
    public int getTodayCases() {
        return todayCases;
    }
    public void setTodayCases(int todayCases) {
        this.todayCases = todayCases;
    }
    public int getDeaths() {
        return deaths;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public int getTodayDeaths() {
        return todayDeaths;
    }
    public void setTodayDeaths(int todayDeaths) {
        this.todayDeaths = todayDeaths;
    }
    public int getRecovered() {
        return recovered;
    }
    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }
    public int getTodayRecovered() {
        return todayRecovered;
    }
    public void setTodayRecovered(int todayRecovered) {
        this.todayRecovered = todayRecovered;
    }
    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
    public int getCritical() {
        return critical;
    }
    public void setCritical(int critical) {
        this.critical = critical;
    }
    public float getCasesPerOneMillion() {
        return casesPerOneMillion;
    }
    public void setCasesPerOneMillion(float casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }
    public float getDeathsPerOneMillion() {
        return deathsPerOneMillion;
    }
    public void setDeathsPerOneMillion(float deathsPerOneMillion) {
        this.deathsPerOneMillion = deathsPerOneMillion;
    }
    public int getTests() {
        return tests;
    }
    public void setTests(int tests) {
        this.tests = tests;
    }
    public float getTestsPerOneMillion() {
        return testsPerOneMillion;
    }
    public void setTestsPerOneMillion(float testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
    }
    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }
    public String getContinent() {
        return continent;
    }
    public void setContinent(String continent) {
        this.continent = continent;
    }
    public int getOneCasePerPeople() {
        return oneCasePerPeople;
    }
    public void setOneCasePerPeople(int oneCasePerPeople) {
        this.oneCasePerPeople = oneCasePerPeople;
    }
    public int getOneDeathPerPeople() {
        return oneDeathPerPeople;
    }
    public void setOneDeathPerPeople(int oneDeathPerPeople) {
        this.oneDeathPerPeople = oneDeathPerPeople;
    }
    public int getOneTestPerPeople() {
        return oneTestPerPeople;
    }
    public void setOneTestPerPeople(int oneTestPerPeople) {
        this.oneTestPerPeople = oneTestPerPeople;
    }
    public float getActivePerOneMillion() {
        return activePerOneMillion;
    }
    public void setActivePerOneMillion(float activePerOneMillion) {
        this.activePerOneMillion = activePerOneMillion;
    }
    public float getRecoveredPerOneMillion() {
        return recoveredPerOneMillion;
    }
    public void setRecoveredPerOneMillion(float recoveredPerOneMillion) {
        this.recoveredPerOneMillion = recoveredPerOneMillion;
    }
    public float getCriticalPerOneMillion() {
        return criticalPerOneMillion;
    }
    public void setCriticalPerOneMillion(float criticalPerOneMillion) {
        this.criticalPerOneMillion = criticalPerOneMillion;
    }
    
}

