package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"option title", "description", "price", "discount"})
public class Package {

    @JsonProperty("option title")
    private String title;
    private String description;
    private String price;
    private String discount;
    @JsonIgnore
    private double annualPrice;

    public Package(String title, String description, String price, String discount, double annualPrice) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.annualPrice = annualPrice;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public double getAnnualPrice() {
        return annualPrice;
    }

}