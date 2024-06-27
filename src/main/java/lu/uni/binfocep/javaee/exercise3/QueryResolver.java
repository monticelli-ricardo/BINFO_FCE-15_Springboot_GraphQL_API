package lu.uni.binfocep.javaee.exercise3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Controller
@Component
public class QueryResolver implements GraphQLQueryResolver {

    // Properties
    private final Covid19DataService covid19DataService;

    // Constructor
    @Autowired
    public QueryResolver(Covid19DataService covid19DataService) {
        this.covid19DataService = covid19DataService;
    }


    // For GraphiQL interface

    // Method to return COVID19 data based on one or several country name(s) and selected subfields
    @QueryMapping
    public List<Map<String, Object>> getCovid19DataByCountry(@Argument List<String> countries) {
        List<Map<String, Object>> allData = covid19DataService.fetchAllData();
        return allData.stream()
                .filter(data -> countries.contains(data.get("country")))
                .collect(Collectors.toList());
    }

    // Method to return COVID19 based on selected subfields
    @QueryMapping
    public List<Map<String, Object>> getCovid19Data() {
        return covid19DataService.fetchAllData();
    }
    
}