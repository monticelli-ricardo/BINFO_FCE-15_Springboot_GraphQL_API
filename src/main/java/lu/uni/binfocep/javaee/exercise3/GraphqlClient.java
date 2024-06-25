package lu.uni.binfocep.javaee.exercise3;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GraphqlClient {

    private final Covid19DataService covid19DataService;

    @Autowired
    public GraphqlClient(Covid19DataService covid19DataService) {
        this.covid19DataService = covid19DataService;
    }

    public List<Covid19Data> fetchCovid19Data(List<String> countries, List<String> selectedFields) throws IOException {
        return covid19DataService.getCovid19DataByCountry(countries, selectedFields);
    }


    public List<String> fetchAvailableCountries() throws IOException{
        return covid19DataService.getAvailableCountries();
    }
}
