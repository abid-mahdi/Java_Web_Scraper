package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Package {

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

    public double getAnnualPrice() {
        return annualPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }
}