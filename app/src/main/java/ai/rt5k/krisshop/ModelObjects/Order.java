package ai.rt5k.krisshop.ModelObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable {
    public ArrayList<Product> products;
    public HashMap<Product, Integer> quantities;
    public String id;
    public double price;
    public String flightNumber;
    public String status;
    public int totalQuantity;
    public String color;
}
