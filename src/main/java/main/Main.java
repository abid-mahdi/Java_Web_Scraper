package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.json.JSONArray;
import org.json.JSONException;
import entities.Package;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String URL = "https://videx.comesconnected.com/";

    public static void main(String[] args) throws JsonProcessingException, JSONException {
        System.out.println(listToJSON(sortByAnnualPrice(scrape())));
    }

    /**
     * Finds all packages on website.
     * @return ArrayList of all packages found.
     */
    public static ArrayList<Package> scrape() {
        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);

        ArrayList<Package> packages = new ArrayList<>();

        try {
            HtmlPage page = client.getPage(URL);
            List<HtmlElement> items = page.getByXPath("//div[@class='package-features']");
            List<HtmlElement> itemTitles = page.getByXPath("//div[@class='header dark-bg']");

            // goes through each package found on website and creates Package object
            for (int i = 0; i < items.size(); i++) {
                HtmlElement title = itemTitles.get(i).getFirstByXPath(".//h3");
                HtmlElement description = items.get(i).getFirstByXPath(".//div[@class='package-name']");
                HtmlElement price = items.get(i).getFirstByXPath(".//div[@class='package-price']");

                // extract Strings from HtmlElements
                String[] splitPriceStr = price.asNormalizedText().split("Save");
                String packageTitle = title.asNormalizedText();
                String packageDescription = description.asNormalizedText()
                        .replaceAll("[\\t\\n\\r]+"," ")
                        .replaceAll("  ", " ")
                        .replaceAll("/", "per");
                String packagePrice = splitPriceStr[0]
                        .replaceAll("[\\t\\n\\r]+"," ")
                        .trim();
                String discount = splitPriceStr.length > 1 ? splitPriceStr[1].trim() : "None";

                // get the price without the pound sign
                String[] splitPrice = packagePrice.split(" ");
                double listedPrice = Double.parseDouble(splitPrice[0].substring(1));

                // workout the annual price
                double annualPrice = 0;
                if (packagePrice.contains("Per Month")) annualPrice = listedPrice * 12;
                else if (packagePrice.contains("Per Year")) annualPrice = listedPrice;

                Package p = new Package(packageTitle, packageDescription,
                        packagePrice, discount, annualPrice);
                packages.add(p);
            }
        } catch (IOException e) {
            System.out.println("Connection error.");
        }

        return packages;
    }

    /**
     * Sorts a list of packages by the annual price of each package.
     * @param listToSort Input list that needs sorting.
     * @return Outputs the sorted list.
     */
    public static List<Package> sortByAnnualPrice(ArrayList<Package> listToSort) {
        return listToSort.stream()
                .sorted(Comparator.comparing(Package::getAnnualPrice))
                .collect(Collectors.toList());
    }

    /**
     * Converts ArrayList to JSON Array and outputs as String.
     * @param packages Input list to create JSON from.
     * @return Formatted JSON array as String.
     */
    public static String listToJSON(List<Package> packages) throws JsonProcessingException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        String uglyJson = objectMapper.writeValueAsString(packages); // convert ArrayList object to JSON as String
        JSONArray json = new JSONArray(uglyJson); // convert text to object
        return json.toString(4); // formats JSON and idents by 4 spaces
    }

}