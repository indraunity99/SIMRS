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
import com.mobile.rsupsanglah.simrsmobile.activity.model.listJenisPerbaikan;

import java.util.ArrayList;

public class listJenisPerbaikanAdapter extends RecyclerView.Adapter<listJenisPerbaikanAdapter.ViewHolder> {

    private Context context;
    private ArrayList<listJenisPerbaikan> listJenisPerbaikanArrayList;

    public listJenisPerbaikanAdapter(Context context, ArrayList<listJenisPerbaikan> listJenisPerbaikanArrayList) {
        this.context = context;
        this.listJenisPerbaikanArrayList = listJenisPerbaikanArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jenis_perbaikan_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listJenisPerbaikan listJenisPerbaikan = listJenisPerbaikanArrayList.get(position);
        holder.menu_jenis_perbaikan.setText(String.valueOf(listJenisPerbaikan.getMenu()));
    }

    @Override
    public int getItemCount() {
        return listJenisPerbaikanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView menu_jenis_perbaikan;

        public ViewHolder(View view) {
            super(view);
            menu_jenis_perbaikan = (TextView) view.findViewById(R.id.menu_jenis_perbaikan);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private listJenisPerbaikanAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final listJenisPerbaikanAdapter.ClickListener clickListener) {
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
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    }
}