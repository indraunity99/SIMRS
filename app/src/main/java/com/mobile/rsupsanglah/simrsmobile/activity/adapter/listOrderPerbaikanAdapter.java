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
import com.mobile.rsupsanglah.simrsmobile.activity.model.listOrderPerbaikan;

import java.util.ArrayList;


public class listOrderPerbaikanAdapter extends RecyclerView.Adapter<listOrderPerbaikanAdapter.ViewHolder> {

    private Context context;
    private ArrayList<listOrderPerbaikan> listAdapterArrayList;

    public listOrderPerbaikanAdapter(Context context, ArrayList<listOrderPerbaikan> listAdapterArrayList) {
        this.context = context;
        this.listAdapterArrayList = listAdapterArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_jenisperbaikan_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listOrderPerbaikan listOrderPerbaikan = listAdapterArrayList.get(position);
        holder.nama_order.setText(String.valueOf(listOrderPerbaikan.getNamaOrder()));
    }

    @Override
    public int getItemCount() {
        return listAdapterArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_order;

        public ViewHolder(View view) {
            super(view);
            nama_order = (TextView) view.findViewById(R.id.nama_order);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private listOrderPerbaikanAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final listOrderPerbaikanAdapter.ClickListener clickListener) {
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
