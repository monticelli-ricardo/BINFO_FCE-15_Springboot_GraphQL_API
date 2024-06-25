package lu.uni.binfocep.javaee.exercise3;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Component
public class QueryResolver implements GraphQLQueryResolver {

    private final Covid19DataService covid19DataService;

    @Autowired
    public QueryResolver(Covid19DataService covid19DataService) {
        this.covid19DataService = covid19DataService;
    }

    public List<Covid19Data> getCovid19Data(List<String> countries, List<String> selectedFields) throws IOException {
        return covid19DataService.getCovid19DataByCountry(countries, selectedFields);
    }

    public List<String> getAvailableCountries() throws IOException  {
        return covid19DataService.getAvailableCountries();
    }
}