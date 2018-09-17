package ai.rt5k.krisshop.ModelObjects;

import android.graphics.Bitmap;

public class Product {
    public int id;
    public String name;
    public float price;
    public Bitmap image;
    public String imageUrl;

    public Product() {}

    public Product(String name, float price) {
        this.name = name;
        this.price = price;
    }
}
