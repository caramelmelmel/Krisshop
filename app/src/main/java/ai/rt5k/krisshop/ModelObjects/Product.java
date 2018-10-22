package ai.rt5k.krisshop.ModelObjects;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Product implements Serializable {
    public int id;
    public String name;
    public double price;
    public int miles;
    public String description;
    public Bitmap image;
    public String imageUrl;

    public Product() {}

    public Product(String name, float price) {
        this.name = name;
        this.price = price;
    }
    @Override
    public int hashCode() {
        return id;
    }
}
