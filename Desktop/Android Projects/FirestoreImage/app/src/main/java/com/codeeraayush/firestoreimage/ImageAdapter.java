package com.codeeraayush.firestoreimage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private Context mContext;
    private List<Upload> mUploads;

    public ImageAdapter(Context mContext, List<Upload> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_to_be,parent,false);
        return new ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Upload upload=mUploads.get(position);
        holder.titleHold.setText(upload.getTitle());
        holder.captionHold.setText(upload.getCaption());
        Log.d("IMAGE_URL", "ImageUrl: "+upload.getImageUrl());
        Picasso.get().load(upload.getImageUrl()).
                fit()
                .centerCrop()
                .into(holder.showImage);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView titleHold;
        public TextView captionHold;
        public ImageView showImage;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            titleHold=itemView.findViewById(R.id.titleHold);
            captionHold=itemView.findViewById(R.id.captionHold);
            showImage=itemView.findViewById(R.id.showImage);

        }
    }

}
