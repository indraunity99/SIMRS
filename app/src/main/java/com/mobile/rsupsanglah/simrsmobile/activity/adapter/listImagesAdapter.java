package com.mobile.rsupsanglah.simrsmobile.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listImages;

import java.util.ArrayList;

public class listImagesAdapter extends RecyclerView.Adapter<listImagesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<listImages> listImagesArrayList;

    public listImagesAdapter(Context context, ArrayList<listImages> listImagesArrayList) {
        this.context = context;
        this.listImagesArrayList = listImagesArrayList;
    }

    @Override
    public listImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_images_row, parent, false);///////////////////////////////////////
        return new listImagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(listImagesAdapter.ViewHolder holder, int position) {
        listImages listImages = listImagesArrayList.get(position);
        holder.nama_images.setText(String.valueOf(listImages.getNama_images()));
    }

    @Override
    public int getItemCount() {
        return listImagesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView nama_images;

        public ViewHolder(View view) {
            super(view);
            nama_images = view.findViewById(R.id.nama_images);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private listImagesAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final listImagesAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    }

    public void clear() {
        int size = this.listImagesArrayList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listImagesArrayList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void removeItem(int position){
        listImagesArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listImagesArrayList.size());
    }

}