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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ai.rt5k.krisshop.ModelObjects.Product;
import ai.rt5k.krisshop.R;
import ai.rt5k.krisshop.Util.ImageThread;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ClickListener clickListener;
    private ClickListener buttonClickListener;

    HashMap<Product, Integer> cartItems;
    public List<Product> products;

    private static DecimalFormat df2 = new DecimalFormat(".00");

    public CartAdapter(HashMap<Product, Integer> cartItems) {
        this.cartItems = cartItems;
        this.products = new ArrayList<>(cartItems.keySet());
    }

    public void setOnClickListener(ClickListener listener) {
        clickListener = listener;
    }

    public void setButtonClickListener(ClickListener listener) {
        buttonClickListener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_cart_item, viewGroup, false);

        return new CartAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        viewHolder.txtName.setText(product.name);
        viewHolder.txtPrice.setText("$" + df2.format(product.price));
        viewHolder.txtQuantity.setText("QTY: " + cartItems.get(product));
        viewHolder.position = i;
        if(product.image != null) {
            viewHolder.imgPicture.setImageBitmap(product.image);
        }
        else if(product.imageUrl != null) {
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
            viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClickListener.onItemClick(viewHolder.position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtPrice, txtQuantity;
        public ImageView imgPicture;
        public Button btnRemove;
        public int position;

        public ViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtCartItemName);
            txtPrice = view.findViewById(R.id.txtCartItemPrice);
            txtQuantity = view.findViewById(R.id.txtCartItemQuantity);
            imgPicture = view.findViewById(R.id.imgCartItem);
            btnRemove = view.findViewById(R.id.btnRemove);
        }
    }
}
