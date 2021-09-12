package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import entities.Package;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

class MainTest {

    private static ArrayList<Package> packages;
    private static List<Package> sortedPackages;
    private static String jsonOut;

    @BeforeAll
    static void init() throws JsonProcessingException, JSONException {
        packages = Main.scrape();
        sortedPackages = Main.sortByAnnualPrice(packages);
        jsonOut = Main.listToJSON(sortedPackages);
    }

    // Test to check output JSON array is correct
    @Test
    void checkJsonMessage() {
        assertEquals("JSON ouput array is incorrect",
                "[\n" +
                        "    {\n" +
                        "        \"price\": \"£66.00 (inc. VAT) Per Year\",\n" +
                        "        \"description\": \"Up to 480 minutes talk time per year including 240 SMS (5p per minute and 4p per SMS thereafter)\",\n" +
                        "        \"discount\": \" £5 on the monthly price\",\n" +
                        "        \"title\": \"Option 480 Mins\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"price\": \"£6.00 (inc. VAT) Per Month\",\n" +
                        "        \"description\": \"Up to 40 minutes talk time per month including 20 SMS (5p per minute and 4p per SMS thereafter)\",\n" +
                        "        \"discount\": \"No discount\",\n" +
                        "        \"title\": \"Option 40 Mins\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"price\": \"£108.00 (inc. VAT) Per Year\",\n" +
                        "        \"description\": \"Up to 2000 minutes talk time per year including 420 SMS (5p per minute and 4p per SMS thereafter)\",\n" +
                        "        \"discount\": \" £12 on the monthly price\",\n" +
                        "        \"title\": \"Option 2000 Mins\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"price\": \"£10.00 (inc. VAT) Per Month\",\n" +
                        "        \"description\": \"Up to 160 minutes talk time per month including 35 SMS (5p per minute and 4p per SMS thereafter)\",\n" +
                        "        \"discount\": \"No discount\",\n" +
                        "        \"title\": \"Option 160 Mins\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"price\": \"£174.00 (inc. VAT) Per Year\",\n" +
                        "        \"description\": \"Up to 3600 minutes talk time per year including 480 SMS (5p per minute and 4p per SMS thereafter)\",\n" +
                        "        \"discount\": \" £18 on the monthly price\",\n" +
                        "        \"title\": \"Option 3600 Mins\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"price\": \"£16.00 (inc. VAT) Per Month\",\n" +
                        "        \"description\": \"300 minutes talk time per month including 40 SMS (5p per minute and 4p per SMS thereafter)\",\n" +
                        "        \"discount\": \"No discount\",\n" +
                        "        \"title\": \"Option 300 Mins\"\n" +
                        "    }\n" +
                        "]",
                jsonOut);
    }

    // Test to check that all the packages have retrieved from website
    @Test
    void checkAllPackagesReceived() {
        assertAll(() -> assertEquals("Number of packages retrieved is wrong", packages.size(), 6),
                      () -> assertTrue("Missing Option 40 Mins", packageWithTitleExists("Option 40 Mins")),
                      () -> assertTrue("Missing Option 160 Mins", packageWithTitleExists("Option 160 Mins")),
                      () -> assertTrue("Missing Option 300 Mins", packageWithTitleExists("Option 300 Mins")),
                      () -> assertTrue("Missing Option 480 Mins", packageWithTitleExists("Option 480 Mins")),
                      () -> assertTrue("Missing Option 2000 Mins", packageWithTitleExists("Option 2000 Mins")),
                      () -> assertTrue("Missing Option 3600 Mins", packageWithTitleExists("Option 3600 Mins")));
    }

    // Test to check the packages have been sorted correctly based on annual price
    @Test
    void checkPackagesAreSorted() {
        assertAll(() -> assertEquals("First element should be Option 480 Mins",
                        "Option 480 Mins", sortedPackages.get(0).getTitle()),
                () -> assertEquals("First element should be Option 40 Mins",
                        "Option 40 Mins", sortedPackages.get(1).getTitle()),
                () -> assertEquals("First element should be Option 2000 Mins",
                        "Option 2000 Mins", sortedPackages.get(2).getTitle()),
                () -> assertEquals("First element should be Option 160 Mins",
                        "Option 160 Mins", sortedPackages.get(3).getTitle()),
                () -> assertEquals("First element should be Option 3600 Mins",
                        "Option 3600 Mins", sortedPackages.get(4).getTitle()),
                () -> assertEquals("First element should be Option 300 Mins",
                        "Option 300 Mins", sortedPackages.get(5).getTitle()));
    }

    private boolean packageWithTitleExists(String title) {
        var titleList = packages
                .stream()
                .filter(p -> p.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
        return titleList.size() > 0;
    }

}