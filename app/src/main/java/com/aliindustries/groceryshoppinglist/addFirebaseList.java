package com.aliindustries.groceryshoppinglist;

public class addFirebaseList {

    int ID;
    String title;
    String item;
    int isChecked;
    int qty;
    double price;

    public addFirebaseList(int ID, String title, String item, int isChecked, int qty, double price) {
        this.ID = ID;
        this.title = title;
        this.item = item;
        this.isChecked = isChecked;
        this.qty = qty;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getItem() {
        return item;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
