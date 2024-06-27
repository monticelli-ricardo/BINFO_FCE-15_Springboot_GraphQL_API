package lu.uni.binfocep.javaee.exercise3;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class Covid19DataView extends VerticalLayout {

    // Properties
    private final Covid19DataService covid19DataService;
    private final MultiSelectComboBox<String> countryComboBox = new MultiSelectComboBox<>();
    private final Grid<Covid19Data> grid = new Grid<>();
    private final CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
    private Set<String> selectedItems = checkboxGroup.getSelectedItems();;
    private List<String> countries = new ArrayList<>();
    private List<String> extraFields = new ArrayList<>(selectedItems);
    private final List<String> items = new ArrayList<>(List.of(
            "updated", "countryInfo", "todayCases", "todayDeaths", "recovered", 
            "todayRecovered", "active", "critical", "casesPerOneMillion", "deathsPerOneMillion", 
            "tests", "testsPerOneMillion", "population", "continent", 
            "oneCasePerPeople", "oneDeathPerPeople", "oneTestPerPeople", 
            "activePerOneMillion", "recoveredPerOneMillion", "criticalPerOneMillion"
    ));

    // View Constructor
    @Autowired
    public Covid19DataView(Covid19DataService covid19DataService) {
        this.covid19DataService = covid19DataService;

        // Add Button components to an horizontal layout
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(
            createFetchButton(),
            createFetchAllButton(),
            createSelectAllFieldsButton(),
            createUnselectAllFieldsButton()
        );
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(true);
        
        // Add View components
        add(
            createTitle(), 
            createMenuSubtitle(), 
            createCountryComboBox(),
            createFieldSelectionComponent(),
            buttonLayout,
            createResultSubtitle(),
            createGrid(extraFields)
        );
    }

    // Methods for View title Components
    private Component createTitle() {
        return new H1("Exercise3: Springboot + GraphQL API");
    }

    private Component createMenuSubtitle() {
        return new H2("Menu");
    }

    private Component createResultSubtitle() {
        return new H2("Results");
    }

    // Methods for Buttons

    // Method to create a Button component to fetch COVID19 data based on selected subfields
    private Button createFetchButton() {
        Button button = new Button("Fetch By Countries");
        button.addClickListener(e -> {
            // Initialize local variables
            countries.clear();
            List<String> temp = new ArrayList<>(countryComboBox.getValue());
            countries.addAll(temp);
            fetchData(countries, extraFields);
        });
        return button;
    }
    // Method to select all subfields except country
    private Button createSelectAllFieldsButton(){
        Button button = new Button("Select All Fields", event -> {
            try {
                // Update UI or display the selected data
                checkboxGroup.clear();
                checkboxGroup.select(items);
            } catch (Exception e) {
                // Handle exception
                e.printStackTrace();
            }
        });
        return button;
    } 

    // Method to select all subfields except country
    private Button createUnselectAllFieldsButton(){
        Button button = new Button("Clear All Fields", event -> {
            try {
                // Clear checkboxes
                checkboxGroup.clear();
            } catch (Exception e) {
                // Handle exception
                e.printStackTrace();
            }
        });
        return button;
    } 

    // Method to fetch COVID19 data from ALL available countries based on selected subfields
    private Button createFetchAllButton(){
        Button button = new Button("Fetch Data");
        button.addClickListener(event -> {
            try{
                countries.clear();
                countries.addAll(fetchAvailableCountries());
                fetchData(countries, items);
            } catch(Exception e){
                // Handle exception
                e.printStackTrace();
            }
        });
        return button;
    } 

    // Select Fields methods

    // Method to configure a Multi-selectable box listing all countries in COVID19 data to be used as input
    private Component createCountryComboBox(){
        countryComboBox.setLabel("Select Country");
        countryComboBox.setPlaceholder("Type to filter or select from the list");
        countryComboBox.setItems(fetchAvailableCountries());
        countryComboBox.setClearButtonVisible(true);
        countryComboBox.setWidth("300px");
        countryComboBox.setAllowCustomValue(true);
        countryComboBox.setErrorMessage("Please select valid country names.");
        countryComboBox.addValueChangeListener(event -> {
            Set<String> selectedCountries = event.getValue();
            List<String> availableCountries = fetchAvailableCountries();
            for (String country : selectedCountries) {
                // Check if input contains numbers
                if (country.matches(".*\\d.*")) {
                    Notification.show("Please select valid country names.", 3000, Notification.Position.TOP_CENTER);
                    countryComboBox.deselect(country);
                    return;
                }
                
                // Check if country is in the available countries list
                if (!availableCountries.contains(country)) {
                    Notification.show("Selected country '" + country + "' is not available.", 3000, Notification.Position.TOP_CENTER);
                    countryComboBox.deselect(country);
                    return;
                }
            }
        });
        return countryComboBox;
    }

    // Method to create a Field group component based on COVID19 subfields
    private Component createFieldSelectionComponent() {
        checkboxGroup.setLabel("Select Fields");
        checkboxGroup.setItems(items);
        return checkboxGroup;
    }

    // Method for Grid component to display COVID19 Data with sortable columns
    private Component createGrid(List<String> extraFields) {
        // Clear the table before displaying it
        grid.removeAllColumns();

        // Add default information
        grid.addColumn(Covid19Data::getCountry).setHeader("Country").setSortable(false);
        grid.addColumn(Covid19Data::getCases).setHeader("Cases").setSortable(false);
        grid.addColumn(Covid19Data::getDeaths).setHeader("Deaths").setSortable(false);

        // Add extra information if selected
        for (String field : extraFields) {
            switch (field) {
                case "updated":
                    grid.addColumn(Covid19Data::getUpdated).setHeader("Updated").setSortable(false);
                    break;
                case "countryInfo":
                    grid.addColumn(data -> data.getCountryInfo().getId()).setHeader("ID").setSortable(false);
                    grid.addColumn(data -> data.getCountryInfo().getIso2()).setHeader("ISO 2").setSortable(false);
                    grid.addColumn(data -> data.getCountryInfo().getIso3()).setHeader("ISO 3").setSortable(false);
                    grid.addColumn(data -> data.getCountryInfo().getLat()).setHeader("Latitude").setSortable(false);
                    grid.addColumn(data -> data.getCountryInfo().getLongg()).setHeader("Longitude").setSortable(false);
                    grid.addColumn(data -> data.getCountryInfo().getFlag()).setHeader("Flag").setSortable(false);
                    break;
                case "todayCases":
                    grid.addColumn(Covid19Data::getTodayCases).setHeader("Today Cases").setSortable(false);
                    break;
                case "todayDeaths":
                    grid.addColumn(Covid19Data::getTodayDeaths).setHeader("Today Deaths").setSortable(false);
                    break;
                case "recovered":
                    grid.addColumn(Covid19Data::getRecovered).setHeader("Recovered").setSortable(false);
                    break;
                case "todayRecovered":
                    grid.addColumn(Covid19Data::getTodayRecovered).setHeader("Today Recovered").setSortable(false);
                    break;
                case "active":
                    grid.addColumn(Covid19Data::getActive).setHeader("Active").setSortable(false);
                    break;
                case "critical":
                    grid.addColumn(Covid19Data::getCritical).setHeader("Critical").setSortable(false);
                    break;
                case "casesPerOneMillion":
                    grid.addColumn(Covid19Data::getCasesPerOneMillion).setHeader("Cases Per One Million").setSortable(false);
                    break;
                case "deathsPerOneMillion":
                    grid.addColumn(Covid19Data::getDeathsPerOneMillion).setHeader("Deaths Per One Million").setSortable(false);
                    break;
                case "tests":
                    grid.addColumn(Covid19Data::getTests).setHeader("Tests").setSortable(false);
                    break;
                case "testsPerOneMillion":
                    grid.addColumn(Covid19Data::getTestsPerOneMillion).setHeader("Tests Per One Million").setSortable(false);
                    break;
                case "population":
                    grid.addColumn(Covid19Data::getPopulation).setHeader("Population").setSortable(false);
                    break;
                case "continent":
                    grid.addColumn(Covid19Data::getContinent).setHeader("Continent").setSortable(false);
                    break;
                case "oneCasePerPeople":
                    grid.addColumn(Covid19Data::getOneCasePerPeople).setHeader("One Case Per People").setSortable(false);
                    break;
                case "oneDeathPerPeople":
                    grid.addColumn(Covid19Data::getOneDeathPerPeople).setHeader("One Death Per People").setSortable(false);
                    break;
                case "oneTestPerPeople":
                    grid.addColumn(Covid19Data::getOneTestPerPeople).setHeader("One Test Per People").setSortable(false);
                    break;
                case "activePerOneMillion":
                    grid.addColumn(Covid19Data::getActivePerOneMillion).setHeader("Active Per One Million").setSortable(false);
                    break;
                case "recoveredPerOneMillion":
                    grid.addColumn(Covid19Data::getRecoveredPerOneMillion).setHeader("Recovered Per One Million").setSortable(false);
                    break;
                case "criticalPerOneMillion":
                    grid.addColumn(Covid19Data::getCriticalPerOneMillion).setHeader("Critical Per One Million").setSortable(false);
                    break;
            }
        }
        
        return grid;
    }

    // Custom Methods to fetch COVID19 information 

    // Get data based on selected subfields from Covid19DataService class
    private void fetchData(List<String> countries, List<String> extraFields) {
        // Update properties
        selectedItems = checkboxGroup.getSelectedItems();
        extraFields.clear();
        for(String item : selectedItems){
            extraFields.add(item);
        }
        if(countries.size()==0){
            Notification.show("No country selected. Please select one or more.", 3000, Notification.Position.TOP_CENTER);
        } else {
            // Update View components and set the new fetched information
            try {
                createGrid(extraFields);
                grid.setItems(createDataProvider(countries, extraFields));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to list all countries in COVID19 data
    private List<String> fetchAvailableCountries() {
        return covid19DataService.getAvailableCountries();
    }

    // Helper method to return a DataProvider elementh that will fetch lazily COVID19 data.
    private DataProvider<Covid19Data, Void> createDataProvider(List<String> countries, List<String> extraFields) throws IOException {
        return new CallbackDataProvider<>(
            query -> {
                // Fetch a specific page of data
                int offset = query.getOffset();
                int limit = query.getLimit();
                List<Covid19Data> page = covid19DataService.getCovid19DataLazyByCountry(offset, limit, countries, extraFields);
                return page.stream();
            },  
            query -> {
                // Fetch the total count of items
                return covid19DataService.getCovid19DataLazyByCountrySize(countries);
            }
        );
    }

}
