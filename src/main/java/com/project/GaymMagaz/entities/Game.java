package com.project.GaymMagaz.entities;

import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int ID;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private double price;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;
    @Column(name = "deleted_at")
    private Date deletedAt;
    @Column(name = "description", length = 4000 )
    private String description;
    @Column(name = "discount_multiplier")
    private double discount;
    @Column(name = "added_date")
    private Date addedDate;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "developer")
    private String developer;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "is_featured")
    private boolean featured;

    public Game() {
    }

    public Game(String name, double price, List<Category> categories, Date deletedAt, String description, double discount, Date addedDate, String publisher, String developer, String imagePath) {
        this.name = name;
        this.price = price;
        this.categories = categories;
        this.deletedAt = deletedAt;
        this.description = description;
        this.discount = discount;
        this.addedDate = addedDate;
        this.publisher = publisher;
        this.developer = developer;
        this.imagePath = imagePath;
        this.featured = false;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String formatAddedDate(){
        String[] date_string = this.addedDate.toString().split("-");
        String monthString;
        switch (date_string[1]) {
            case "01":
                monthString = "January";
                break;
            case "02":
                monthString = "February";
                break;
            case "03":
                monthString = "March";
                break;
            case "04":
                monthString = "April";
                break;
            case "05":
                monthString = "May";
                break;
            case "06":
                monthString = "June";
                break;
            case "07":
                monthString = "July";
                break;
            case "08":
                monthString = "August";
                break;
            case "09":
                monthString = "September";
                break;
            case "10":
                monthString = "October";
                break;
            case "11":
                monthString = "November";
                break;
            case "12":
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        if (date_string[2].startsWith("0")) {
            date_string[2] = date_string[2].replace("0", "");
        }
        return date_string[2] + " " + monthString + " " + date_string[0];
    }

    public String getCategoriesNames(){
        List<Category> categories = this.categories;
        String result = "";
        for(Category c : categories){
            if (categories.get(0) != c){
                result += ", " + c.getName();
            }
            else {
                result += c.getName();
            }
        }
        return result;
    }

    public String formatDiscount(){
        double discount = this.discount;
        int discount_int = (int)(discount*100);
        return "-" + String.valueOf(discount_int) + "%";
    }

    public double calculatePrice(){
        double price = this.price;
        double discount = this.discount;
        if (discount != 0){
            price = price*(1-discount);
        }
        return Math.floor(price * 100) / 100;
    }
}
