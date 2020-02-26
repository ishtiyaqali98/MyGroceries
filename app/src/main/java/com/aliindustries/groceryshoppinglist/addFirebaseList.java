package com.aliindustries.groceryshoppinglist;

public class addFirebaseList {

    int ID;
    String title;
    String item;
    int isChecked;
    int qty;
    double price;
    String week;
    String month;
    int year;
    long dateinms;
    String datecreated;

    public addFirebaseList(int ID, String title, String item, int isChecked, int qty, double price, String week, String month, int year, long dateinms, String datecreated) {
        this.ID = ID;
        this.title = title;
        this.item = item;
        this.isChecked = isChecked;
        this.qty = qty;
        this.price = price;
        this.week = week;
        this.month = month;
        this.year = year;
        this.dateinms = dateinms;
        this.datecreated = datecreated;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getDateinms() {
        return dateinms;
    }

    public void setDateinms(long dateinms) {
        this.dateinms = dateinms;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    @Override
    public String toString() {
        return "addFirebaseList{" +
                "ID=" + ID +
                ", title='" + title + '\'' +
                ", item='" + item + '\'' +
                ", isChecked=" + isChecked +
                ", qty=" + qty +
                ", price=" + price +
                ", week='" + week + '\'' +
                ", month='" + month + '\'' +
                ", year=" + year +
                ", dateinms=" + dateinms +
                ", datecreated='" + datecreated + '\'' +
                '}';
    }
}
