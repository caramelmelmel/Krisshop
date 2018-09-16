package ai.rt5k.krisshop.RecyclerViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.R;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.ViewHolder> {
    ArrayList<Product> products;
    private static DecimalFormat df2 = new DecimalFormat(".00");

    public BestSellerAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_product_bestseller, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        viewHolder.txtName.setText(product.name);
        viewHolder.txtPrice.setText("$" + df2.format(product.price));
        if(product.image != null) {
            viewHolder.imgPicture.setImageBitmap(product.image);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPrice;
        public ImageView imgPicture;

        public ViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtPrice);
            imgPicture = view.findViewById(R.id.imgPicture);
        }
    }
}
