package ai.rt5k.krisshop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.R;
import ai.rt5k.krisshop.RecyclerViewAdapters.BestSellerAdapter;
import ai.rt5k.krisshop.RecyclerViewAdapters.ClickListener;
import ai.rt5k.krisshop.RecyclerViewAdapters.ProductAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerHomeFragment extends Fragment {
    RecyclerView lstBestSellers, lstNewProducts;

    // TODO: Remove dummy data
    String[] names = {"PRECIOUS MOMENTS \"SINGAPORE GIRL\" (70TH ANNIVERSARY)", "Product 2", "Product 3", "Product 4"};
    float[] prices = {10.0f, 1.0f, 2.55f, 10.70f};

    ArrayList<Product> bestSellers;
    BestSellerAdapter bestSellerAdapter;
    ProductAdapter newProductAdapter;

    public CustomerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_customer_home, container, false);
        lstBestSellers = itemView.findViewById(R.id.lstBestSellers);
        lstNewProducts = itemView.findViewById(R.id.lstNewProducts);

        bestSellers = new ArrayList<>();

        // TODO: Get actual products from backend
        for(int i = 0; i < names.length; i++) {
            Product p = new Product();
            p.name = names[i];
            p.price = prices[i];

            bestSellers.add(p);
        }

        bestSellerAdapter = new BestSellerAdapter(bestSellers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        lstBestSellers.setLayoutManager(mLayoutManager);
        lstBestSellers.setItemAnimator(new DefaultItemAnimator());
        lstBestSellers.setAdapter(bestSellerAdapter);

        newProductAdapter = new ProductAdapter(bestSellers);
        newProductAdapter.setOnClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CustomerHomeActivity", position + "");
            }
        });
        RecyclerView.LayoutManager productManager = new GridLayoutManager(getActivity(), 2);
        lstNewProducts.setLayoutManager(productManager);
        lstNewProducts.setItemAnimator(new DefaultItemAnimator());
        lstNewProducts.setAdapter(newProductAdapter);

        return itemView;
    }

}
