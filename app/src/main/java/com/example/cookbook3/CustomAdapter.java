package com.example.cookbook3;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    // Create a view holder class
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Create a constructor
        public MyViewHolder() {
            super(null);
        }
    }

    // Bind the view holder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
    }

    // Get the item count
    @Override
    public int getItemCount() {
        return 0;
    }
}
