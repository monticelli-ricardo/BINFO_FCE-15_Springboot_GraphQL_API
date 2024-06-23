package lu.uni.binfocep.javaee.exercise3;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class CovidDataView extends VerticalLayout {

    private final GraphqlClient graphqlClient;

    private TextField countryInput;
    private Button fetchDataButton;

    @Autowired
    public CovidDataView(GraphqlClient graphqlClient) {
        this.graphqlClient = graphqlClient;
        
        // Initialize UI components
        countryInput = new TextField("Enter countries (comma-separated):");
        fetchDataButton = new Button("Fetch Data");
        fetchDataButton.addClickListener(event -> fetchData());

        // Add components to the layout
        add(countryInput, fetchDataButton);
    }

    private void fetchData() {
        String countries = countryInput.getValue();
        try {
            // Split the input string into an array of country names
            String[] countryArray = countries.split(",");

            // Fetch COVID-19 data for the specified countries
            List<Covid19Data> covidData = graphqlClient.getCovid19DataByCountry(List.of(countryArray));

            if (covidData != null && !covidData.isEmpty()) {
                // Display data using a Grid
                // Example: Displaying country names and total cases
                StringBuilder message = new StringBuilder();
                for (Covid19Data data : covidData) {
                    message.append(data.getCountry()).append(": ").append(data.getCases()).append(" cases\n");
                }
                Notification.show(message.toString());
            } else {
                Notification.show("No COVID-19 data available for the specified countries.");
            }
        } catch (Exception e) {
            Notification.show("Error fetching COVID-19 data: " + e.getMessage());
        }
    }
}

