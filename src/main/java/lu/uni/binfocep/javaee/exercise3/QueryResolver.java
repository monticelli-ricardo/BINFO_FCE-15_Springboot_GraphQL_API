package lu.uni.binfocep.javaee.exercise3;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Component
public class QueryResolver implements GraphQLQueryResolver {

    @Autowired
    private Covid19DataService covid19DataService;

    public List<Covid19Data> getCovid19DataByCountry(List<String> countries) throws IOException {
        // Call the service method with the list of countries
        return covid19DataService.getCovid19DataByCountry(countries);
    }

    // Overload method to handle single country input as a string
    public List<Covid19Data> getCovid19DataByCountry(String country) throws IOException {
        return getCovid19DataByCountry(Arrays.asList(country));
    }
    
}