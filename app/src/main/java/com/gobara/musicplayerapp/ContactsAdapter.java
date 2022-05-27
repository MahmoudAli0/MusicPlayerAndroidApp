package com.gobara.musicplayerapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.gobara.musicplayerapp.databinding.CustomContactItemBinding;


import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private ArrayList<ContactItem> contactItems;

    public ContactsAdapter(ArrayList<ContactItem> contactItems) {
        this.contactItems = contactItems;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomContactItemBinding binding = CustomContactItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        ContactViewHolder holder = new ContactViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactItem contactItem = contactItems.get(position);

        if (contactItem.getName() != null) {
            holder.binding.contactItemTvName.setText(contactItem.getName());
        }

        if (contactItem.getNumber() != null) {
            holder.binding.contactItemTvNumber.setText ( contactItem.getNumber () );
        }
        Bitmap image = null;
        if ( contactItem.getPhoto() != null && !contactItem.getPhoto().equals("")) {
            image = BitmapFactory.decodeFile(contactItem.getPhoto());
            if (image != null)
                holder.binding.contactItemIv.setImageBitmap(image);
            else {
                image = BitmapFactory.decodeResource(holder.binding.getRoot().getContext().getResources(),
                        com.google.android.material.R.drawable.abc_ic_arrow_drop_right_black_24dp);
                holder.binding.contactItemIv.setImageBitmap(image);
            }
        } else {
            image = BitmapFactory.decodeResource(holder.binding.getRoot().getContext().getResources(),
                    com.google.android.material.R.drawable.abc_ic_arrow_drop_right_black_24dp);
            holder.binding.contactItemIv.setImageBitmap(image);
        }
    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        CustomContactItemBinding binding;

        public ContactViewHolder(@NonNull CustomContactItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
