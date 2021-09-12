package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import entities.Package;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

    // Test to check output JSON is correct
    @Test
    void checkJsonMessage() throws JSONException {
        JSONObject option480 = new JSONObject();
        option480.put("description", "Up to 480 minutes talk time per year including 240 SMS (5p per minute and 4p per SMS thereafter)");
        option480.put("discount", "\u00A35 on the monthly price");
        option480.put("option title", "Option 480 Mins");
        option480.put("price", "\u00A366.00 (inc. VAT) Per Year");

        JSONObject option40 = new JSONObject();
        option40.put("description", "Up to 40 minutes talk time per month including 20 SMS (5p per minute and 4p per SMS thereafter)");
        option40.put("discount", "None");
        option40.put("option title", "Option 40 Mins");
        option40.put("price", "\u00A36.00 (inc. VAT) Per Month");

        JSONObject option2000 = new JSONObject();
        option2000.put("description", "Up to 2000 minutes talk time per year including 420 SMS (5p per minute and 4p per SMS thereafter)");
        option2000.put("discount", "\u00A312 on the monthly price");
        option2000.put("option title", "Option 2000 Mins");
        option2000.put("price", "\u00A3108.00 (inc. VAT) Per Year");

        JSONObject option160 = new JSONObject();
        option160.put("description", "Up to 160 minutes talk time per month including 35 SMS (5p per minute and 4p per SMS thereafter)");
        option160.put("discount", "None");
        option160.put("option title", "Option 160 Mins");
        option160.put("price", "\u00A310.00 (inc. VAT) Per Month");

        JSONObject option3600 = new JSONObject();
        option3600.put("description", "Up to 3600 minutes talk time per year including 480 SMS (5p per minute and 4p per SMS thereafter)");
        option3600.put("discount", "\u00A318 on the monthly price");
        option3600.put("option title", "Option 3600 Mins");
        option3600.put("price", "\u00A3174.00 (inc. VAT) Per Year");

        JSONObject option300 = new JSONObject();
        option300.put("description", "300 minutes talk time per month including 40 SMS (5p per minute and 4p per SMS thereafter)");
        option300.put("discount", "None");
        option300.put("option title", "Option 300 Mins");
        option300.put("price", "\u00A316.00 (inc. VAT) Per Month");

        JSONArray expectedArray = new JSONArray();
        expectedArray.put(option480);
        expectedArray.put(option40);
        expectedArray.put(option2000);
        expectedArray.put(option160);
        expectedArray.put(option3600);
        expectedArray.put(option300);

        JSONArray actualOutput = new JSONArray(jsonOut);
        assertEquals("Wrong JSON returned",
                expectedArray.toString(),
                actualOutput.toString());
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