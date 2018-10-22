package ai.rt5k.krisshop.RecyclerViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import ai.rt5k.krisshop.Util.ImageThread;
import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    ArrayList<Product> products;
    private static DecimalFormat df2 = new DecimalFormat(".00");
    private ClickListener clickListener;
    private ClickListener buttonClickListener;

    public ProductAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    public void setOnClickListener(ClickListener listener) {
        clickListener = listener;
    }
    public void setButtonClickListener(ClickListener listener) {
        buttonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_product_general, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        viewHolder.txtName.setText(product.name);
        viewHolder.txtPrice.setText("$" + df2.format(product.price));
        viewHolder.txtMiles.setText(NumberFormat.getNumberInstance(Locale.US).format(product.miles) + " miles ");
        viewHolder.position = i;
        if(product.image != null) {
            viewHolder.imgPicture.setImageBitmap(product.image);
        }
        else if(product.imageUrl != null) {
            Log.d("ProductAdapter", "Starting thread");
            ImageThread t = new ImageThread(product.imageUrl, viewHolder.imgPicture);
            t.start();
        }

        if(clickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(viewHolder.position);
                }
            });
        }
        if(buttonClickListener != null) {
            viewHolder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClickListener.onItemClick(viewHolder.position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int position;

        public View itemView;
        public TextView txtName, txtPrice, txtMiles;
        public ImageView imgPicture;
        public Button btnAddToCart;

        public ViewHolder(View view) {
            super(view);
            itemView = view;
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtMiles = view.findViewById(R.id.txtMiles);
            imgPicture = view.findViewById(R.id.imgPicture);
            btnAddToCart = view.findViewById(R.id.btnAddToCart);
        }
    }
}
