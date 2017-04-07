package com.example.satwik.roomie;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Daniel on 28-03-2017.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private ArrayList<Item> items;

    public DataAdapter(ArrayList<Item> items)
    {
        this.items=items;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Item curr=items.get(position);
      //  holder.itemImage.setImageBitmap(curr.getImage());
        holder.itemTitle.setText(curr.getItemName());
        holder.itemPrice.setText(curr.getItemPrice());
        holder.itemPrice.setText(curr.getItemQuant());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(Item item) {
        items.add(item);
        notifyItemInserted(items.size());
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemPrice;
        private TextView itemQuant;
        public ViewHolder(View view) {
            super(view);

            itemTitle = (TextView)view.findViewById(R.id.itemTitle);
            itemImage= (ImageView) view.findViewById(R.id.itemImage);
            itemPrice= (TextView) view.findViewById(R.id.itemPrice);
            itemQuant= (TextView) view.findViewById(R.id.itemQuant);

        }
    }
}
