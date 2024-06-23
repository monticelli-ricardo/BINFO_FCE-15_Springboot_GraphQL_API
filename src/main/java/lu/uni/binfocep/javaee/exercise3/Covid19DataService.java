package lu.uni.binfocep.javaee.exercise3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Covid19DataService {
    private static final Logger logger = Logger.getLogger(Covid19DataService.class.getName());
    private static final String COVID_API_URL = "https://disease.sh/v3/covid-19/countries";
    private Map<String, Covid19Data> cache = new ConcurrentHashMap<>();

    @Cacheable("covid19Data")
    public List<Covid19Data> getCovid19DataByCountry(List<String> countries) throws IOException {
        logger.info("Fetching data for countries: " + countries);
        List<Covid19Data> result = new ArrayList<>();
        fetchAndCacheData();
        for (String country : countries) {
            if (cache.containsKey(country)) {
                logger.info("Data found in cache for country: " + country);
                result.add(cache.get(country));
            } else {
                logger.warning("Data not found in cache for country: " + country);
                result.add(null);
            }
        }
        return result;
    }

    private void fetchAndCacheData() throws IOException {
        logger.info("Fetching data from API: " + COVID_API_URL);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(COVID_API_URL, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            for (JsonNode node : root) {
                Covid19Data data = mapper.treeToValue(node, Covid19Data.class);
                logger.info("Caching data for country: " + data.getCountry());
                cache.put(data.getCountry(), data);
            }
        } else {
            logger.severe("Failed to fetch data from API. Status code: " + response.getStatusCode());
        }
    }
}
