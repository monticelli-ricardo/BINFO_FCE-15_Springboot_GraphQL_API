package lu.uni.binfocep.javaee.exercise3;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class Covid19DataView extends VerticalLayout {
    // Variables
    private final Covid19DataService covid19DataService;
    private final MultiSelectComboBox<String> countryComboBox = new MultiSelectComboBox<>();
    private final Grid<Covid19Data> grid = new Grid<>();
    private final CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();;
    private Set<String> selectedItems = checkboxGroup.getSelectedItems();;
    private List<String> extraFields = new ArrayList<>(selectedItems);

    // View Constructor
    @Autowired
    public Covid19DataView(Covid19DataService covid19DataService) {
        this.covid19DataService = covid19DataService;
        add(createTitle(), createSubtitle(), createFetchButton(), createCountryComboBox(), createFieldSelectionComponent(), createGrid(extraFields));
    }

    // Methods to create View Components
    private Component createTitle() {
        return new H1("Exercise3");
    }

    private Component createSubtitle() {
        return new H2("Menu");
    }

    private Button createFetchButton() {
        Button button = new Button("Fetch Data");
        button.addClickListener(e -> {
            fetchData(countryComboBox.getValue(), extraFields);
        });
        return button;
    }

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

    private Component createFieldSelectionComponent() {
        checkboxGroup.setLabel("Select Fields");
        checkboxGroup.setItems(
                "updated",
                "countryInfo",
                "todayCases",
                "todayDeaths",
                "recovered",
                "todayRecovered",
                "active",
                "critical",
                "casesPerOneMillion",
                "deathsPerOneMillion",
                "tests",
                "testsPerOneMillion",
                "population",
                "continent",
                "oneCasePerPeople",
                "oneDeathPerPeople",
                "oneTestPerPeople",
                "activePerOneMillion",
                "recoveredPerOneMillion",
                "criticalPerOneMillion"
        );
        return checkboxGroup;
    }


    private Component createGrid(List<String> extraFields) {
        // Clear the table before displaying it
        grid.removeAllColumns();

        // Add default information
        grid.addColumn(Covid19Data::getCountry).setHeader("Country");
        grid.addColumn(Covid19Data::getCases).setHeader("Cases");
        grid.addColumn(Covid19Data::getDeaths).setHeader("Deaths");

        // Add extra information if selected
        for (String field : extraFields) {
            switch (field) {
                case "updated":
                    grid.addColumn(Covid19Data::getUpdated).setHeader("Updated");
                    break;
                case "countryInfo":
                    grid.addColumn(data -> data.getCountryInfo().getId()).setHeader("ID");
                    grid.addColumn(data -> data.getCountryInfo().getIso2()).setHeader("ISO 2");
                    grid.addColumn(data -> data.getCountryInfo().getIso3()).setHeader("ISO 3");
                    grid.addColumn(data -> data.getCountryInfo().getLat()).setHeader("Latitude");
                    grid.addColumn(data -> data.getCountryInfo().getLongg()).setHeader("Longitude");
                    grid.addColumn(data -> data.getCountryInfo().getFlag()).setHeader("Flag");
                    break;
                case "todayCases":
                    grid.addColumn(Covid19Data::getTodayCases).setHeader("Today Cases");
                    break;
                case "todayDeaths":
                    grid.addColumn(Covid19Data::getTodayDeaths).setHeader("Today Deaths");
                    break;
                case "recovered":
                    grid.addColumn(Covid19Data::getRecovered).setHeader("Recovered");
                    break;
                case "todayRecovered":
                    grid.addColumn(Covid19Data::getTodayRecovered).setHeader("Today Recovered");
                    break;
                case "active":
                    grid.addColumn(Covid19Data::getActive).setHeader("Active");
                    break;
                case "critical":
                    grid.addColumn(Covid19Data::getCritical).setHeader("Critical");
                    break;
                case "casesPerOneMillion":
                    grid.addColumn(Covid19Data::getCasesPerOneMillion).setHeader("Cases Per One Million");
                    break;
                case "deathsPerOneMillion":
                    grid.addColumn(Covid19Data::getDeathsPerOneMillion).setHeader("Deaths Per One Million");
                    break;
                case "tests":
                    grid.addColumn(Covid19Data::getTests).setHeader("Tests");
                    break;
                case "testsPerOneMillion":
                    grid.addColumn(Covid19Data::getTestsPerOneMillion).setHeader("Tests Per One Million");
                    break;
                case "population":
                    grid.addColumn(Covid19Data::getPopulation).setHeader("Population");
                    break;
                case "continent":
                    grid.addColumn(Covid19Data::getContinent).setHeader("Continent");
                    break;
                case "oneCasePerPeople":
                    grid.addColumn(Covid19Data::getOneCasePerPeople).setHeader("One Case Per People");
                    break;
                case "oneDeathPerPeople":
                    grid.addColumn(Covid19Data::getOneDeathPerPeople).setHeader("One Death Per People");
                    break;
                case "oneTestPerPeople":
                    grid.addColumn(Covid19Data::getOneTestPerPeople).setHeader("One Test Per People");
                    break;
                case "activePerOneMillion":
                    grid.addColumn(Covid19Data::getActivePerOneMillion).setHeader("Active Per One Million");
                    break;
                case "recoveredPerOneMillion":
                    grid.addColumn(Covid19Data::getRecoveredPerOneMillion).setHeader("Recovered Per One Million");
                    break;
                case "criticalPerOneMillion":
                    grid.addColumn(Covid19Data::getCriticalPerOneMillion).setHeader("Critical Per One Million");
                    break;
            }
        }
        return grid;
    }

    // Custom Methods to fetch COVID19 information

    private void fetchData(Set<String> country, List<String> extraFields) {
        // Initialize local variables
        List<String> countries = new ArrayList<>(country);
        // Update variables based on new search parameters
        selectedItems.clear();
        selectedItems = checkboxGroup.getSelectedItems();
        extraFields.clear();
        for(String item : selectedItems){
            extraFields.add(item);
        }
        List<Covid19Data> data = covid19DataService.getCovid19DataByCountry(countries, extraFields);
        createGrid(extraFields);
        grid.setItems(data);
    }

    private List<String> fetchAvailableCountries() {
        return covid19DataService.getAvailableCountries();
    }
}
