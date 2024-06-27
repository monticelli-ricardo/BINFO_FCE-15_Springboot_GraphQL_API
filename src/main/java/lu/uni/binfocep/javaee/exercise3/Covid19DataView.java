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
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.function.SerializableComparator;

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
        Button button = new Button("Fetch Data");
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
                checkboxGroup.select(items);
                // Update UI or display the selected data
            } catch (Exception e) {
                // Handle exception
            }
        });
        return button;
    } 

    // Method to select all subfields except country
    private Button createUnselectAllFieldsButton(){
        Button button = new Button("Clear All Fields", event -> {
            try {
                checkboxGroup.clear();
                // Update UI or display the selected data
            } catch (Exception e) {
                // Handle exception
            }
        });
        return button;
    } 

    // Method to fetch COVID19 data from ALL available countries based on selected subfields
    private Button createFetchAllButton(){
        Button button = new Button("Fetch All");
        button.addClickListener(e -> {
            countries.clear();
            countries.addAll(fetchAvailableCountries());
            fetchData(countries, items);
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
        grid.addColumn(Covid19Data::getCountry).setHeader("Country").setSortable(true);
        grid.addColumn(Covid19Data::getCases).setHeader("Cases").setSortable(true);
        grid.addColumn(Covid19Data::getDeaths).setHeader("Deaths").setSortable(true);

        // Add extra information if selected
        for (String field : extraFields) {
            switch (field) {
                case "updated":
                    grid.addColumn(Covid19Data::getUpdated).setHeader("Updated").setSortable(true);
                    break;
                case "countryInfo":
                    grid.addColumn(data -> data.getCountryInfo().getId()).setHeader("ID").setSortable(true);
                    grid.addColumn(data -> data.getCountryInfo().getIso2()).setHeader("ISO 2").setSortable(true);
                    grid.addColumn(data -> data.getCountryInfo().getIso3()).setHeader("ISO 3").setSortable(true);
                    grid.addColumn(data -> data.getCountryInfo().getLat()).setHeader("Latitude").setSortable(true);
                    grid.addColumn(data -> data.getCountryInfo().getLongg()).setHeader("Longitude").setSortable(true);
                    grid.addColumn(data -> data.getCountryInfo().getFlag()).setHeader("Flag").setSortable(true);
                    break;
                case "todayCases":
                    grid.addColumn(Covid19Data::getTodayCases).setHeader("Today Cases").setSortable(true);
                    break;
                case "todayDeaths":
                    grid.addColumn(Covid19Data::getTodayDeaths).setHeader("Today Deaths").setSortable(true);
                    break;
                case "recovered":
                    grid.addColumn(Covid19Data::getRecovered).setHeader("Recovered").setSortable(true);
                    break;
                case "todayRecovered":
                    grid.addColumn(Covid19Data::getTodayRecovered).setHeader("Today Recovered").setSortable(true);
                    break;
                case "active":
                    grid.addColumn(Covid19Data::getActive).setHeader("Active").setSortable(true);
                    break;
                case "critical":
                    grid.addColumn(Covid19Data::getCritical).setHeader("Critical").setSortable(true);
                    break;
                case "casesPerOneMillion":
                    grid.addColumn(Covid19Data::getCasesPerOneMillion).setHeader("Cases Per One Million").setSortable(true);
                    break;
                case "deathsPerOneMillion":
                    grid.addColumn(Covid19Data::getDeathsPerOneMillion).setHeader("Deaths Per One Million").setSortable(true);
                    break;
                case "tests":
                    grid.addColumn(Covid19Data::getTests).setHeader("Tests").setSortable(true);
                    break;
                case "testsPerOneMillion":
                    grid.addColumn(Covid19Data::getTestsPerOneMillion).setHeader("Tests Per One Million").setSortable(true);
                    break;
                case "population":
                    grid.addColumn(Covid19Data::getPopulation).setHeader("Population").setSortable(true);
                    break;
                case "continent":
                    grid.addColumn(Covid19Data::getContinent).setHeader("Continent").setSortable(true);
                    break;
                case "oneCasePerPeople":
                    grid.addColumn(Covid19Data::getOneCasePerPeople).setHeader("One Case Per People").setSortable(true);
                    break;
                case "oneDeathPerPeople":
                    grid.addColumn(Covid19Data::getOneDeathPerPeople).setHeader("One Death Per People").setSortable(true);
                    break;
                case "oneTestPerPeople":
                    grid.addColumn(Covid19Data::getOneTestPerPeople).setHeader("One Test Per People").setSortable(true);
                    break;
                case "activePerOneMillion":
                    grid.addColumn(Covid19Data::getActivePerOneMillion).setHeader("Active Per One Million").setSortable(true);
                    break;
                case "recoveredPerOneMillion":
                    grid.addColumn(Covid19Data::getRecoveredPerOneMillion).setHeader("Recovered Per One Million").setSortable(true);
                    break;
                case "criticalPerOneMillion":
                    grid.addColumn(Covid19Data::getCriticalPerOneMillion).setHeader("Critical Per One Million").setSortable(true);
                    break;
            }
        }
        // Create data provider with initial settings
        grid.setDataProvider(createDataProvider(countries, extraFields));

        // Add a listener to handle column sorting
        grid.addSortListener(event -> {
            // Update data provider with sorted data
            grid.setDataProvider(createDataProvider(countries, extraFields));
        });
        
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to list all countries in COVID19 data
    private List<String> fetchAvailableCountries() {
        return covid19DataService.getAvailableCountries();
    }

    // Helper method to return a DataProvider elementh that will fetch lazily COVID19 data.
    private DataProvider<Covid19Data, Void> createDataProvider(List<String> countries, List<String> extraFields) {
        return new CallbackDataProvider<>(
            query -> {
                int offset = query.getOffset();
                int limit = query.getLimit();
    
                // Get sort orders from the query
                List<QuerySortOrder> sortOrders = query.getSortOrders();
    
                // Create a comparator based on sort orders
                SerializableComparator<Covid19Data> comparator = createComparator(sortOrders);
    
                // Fetch data based on offset, limit, countries, and extra fields
                List<Covid19Data> data = covid19DataService.getCovid19DataLazyByCountry(offset, limit, countries, extraFields);
    
                // Apply sorting if comparator is available
                if (comparator != null) {
                    data.sort(comparator);
                }
    
                return data.stream();
            },
            query -> covid19DataService.getCovid19DataLazyByCountrySize(countries)
        );
    }
    
    


    // Adjust createComparator method to return SerializableComparator
    private SerializableComparator<Covid19Data> createComparator(List<QuerySortOrder> sortOrders) {
        if (sortOrders.isEmpty()) {
            return null;
        }

        QuerySortOrder sortOrder = sortOrders.get(0); // Assuming single column sorting for simplicity
        String sortedProperty = sortOrder.getSorted();
        SortDirection direction = sortOrder.getDirection();

        // Implement comparator based on sorted property and direction
        SerializableComparator<Covid19Data> comparator = null;
        switch (sortedProperty) {
            case "updated":
                comparator = (o1, o2) -> {
                    Long value1 = o1.getUpdated();
                    Long value2 = o2.getUpdated();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "countryInfo.id":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getCountryInfo().getId();
                    Integer value2 = o2.getCountryInfo().getId();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "countryInfo.iso2":
                comparator = (o1, o2) -> {
                    String value1 = o1.getCountryInfo().getIso2();
                    String value2 = o2.getCountryInfo().getIso2();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "countryInfo.iso3":
                comparator = (o1, o2) -> {
                    String value1 = o1.getCountryInfo().getIso3();
                    String value2 = o2.getCountryInfo().getIso3();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "countryInfo.lat":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getCountryInfo().getLat();
                    Float value2 = o2.getCountryInfo().getLat();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "countryInfo.longg":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getCountryInfo().getLongg();
                    Float value2 = o2.getCountryInfo().getLongg();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "countryInfo.flag":
                comparator = (o1, o2) -> {
                    String value1 = o1.getCountryInfo().getFlag();
                    String value2 = o2.getCountryInfo().getFlag();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            
            case "country":
                comparator = (o1, o2) -> {
                    String value1 = o1.getCountry();
                    String value2 = o2.getCountry();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "cases":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getCases();
                    Integer value2 = o2.getCases();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "todayCases":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getTodayCases();
                    Integer value2 = o2.getTodayCases();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "deaths":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getDeaths();
                    Integer value2 = o2.getDeaths();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "todayDeaths":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getTodayDeaths();
                    Integer value2 = o2.getTodayDeaths();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "recovered":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getRecovered();
                    Integer value2 = o2.getRecovered();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "todayRecovered":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getTodayRecovered();
                    Integer value2 = o2.getTodayRecovered();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "active":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getActive();
                    Integer value2 = o2.getActive();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "critical":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getCritical();
                    Integer value2 = o2.getCritical();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "casesPerOneMillion":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getCasesPerOneMillion();
                    Float value2 = o2.getCasesPerOneMillion();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "deathsPerOneMillion":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getDeathsPerOneMillion();
                    Float value2 = o2.getDeathsPerOneMillion();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "tests":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getTests();
                    Integer value2 = o2.getTests();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "testsPerOneMillion":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getTestsPerOneMillion();
                    Float value2 = o2.getTestsPerOneMillion();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "population":
                comparator = (o1, o2) -> {
                    Long value1 = o1.getPopulation();
                    Long value2 = o2.getPopulation();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "continent":
                comparator = (o1, o2) -> {
                    String value1 = o1.getContinent();
                    String value2 = o2.getContinent();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "oneCasePerPeople":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getOneCasePerPeople();
                    Integer value2 = o2.getOneCasePerPeople();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "oneDeathPerPeople":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getOneDeathPerPeople();
                    Integer value2 = o2.getOneDeathPerPeople();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "oneTestPerPeople":
                comparator = (o1, o2) -> {
                    Integer value1 = o1.getOneTestPerPeople();
                    Integer value2 = o2.getOneTestPerPeople();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "activePerOneMillion":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getActivePerOneMillion();
                    Float value2 = o2.getActivePerOneMillion();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "recoveredPerOneMillion":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getRecoveredPerOneMillion();
                    Float value2 = o2.getRecoveredPerOneMillion();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            case "criticalPerOneMillion":
                comparator = (o1, o2) -> {
                    Float value1 = o1.getCriticalPerOneMillion();
                    Float value2 = o2.getCriticalPerOneMillion();
                    return direction == SortDirection.ASCENDING ? value1.compareTo(value2) : value2.compareTo(value1);
                };
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sortedProperty);
        }

        return comparator;
    }

}
