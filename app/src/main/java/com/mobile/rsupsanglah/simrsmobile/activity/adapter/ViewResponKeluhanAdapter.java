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
import com.mobile.rsupsanglah.simrsmobile.activity.model.ViewResponKeluhan;

import java.util.ArrayList;

public class ViewResponKeluhanAdapter extends RecyclerView.Adapter<ViewResponKeluhanAdapter.ViewHolder> {

    RecyclerView recyclerView;
    private Context context;
    private ArrayList<ViewResponKeluhan> listResponKeluhanArrayListView;

    public ViewResponKeluhanAdapter(Context context, ArrayList<ViewResponKeluhan> listResponKeluhanArrayListView) {
        this.context = context;
        this.listResponKeluhanArrayListView = listResponKeluhanArrayListView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_respon_keluhan_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewResponKeluhan ViewResponKeluhan = listResponKeluhanArrayListView.get(position);
        holder.nama_history_pekerjaan.setText(String.valueOf(ViewResponKeluhan.getJenis_pekerjaan()));
        holder.tgl_mulai.setText(String.valueOf(ViewResponKeluhan.getWaktu_mulai()));
        holder.tgl_selesai.setText(String.valueOf(ViewResponKeluhan.getWaktu_selesai()));
    }

    @Override
    public int getItemCount() {
        return listResponKeluhanArrayListView.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_history_pekerjaan, tgl_mulai, tgl_selesai;

        public ViewHolder(View view) {
            super(view);
            nama_history_pekerjaan = (TextView) view.findViewById(R.id.nama_history_pekerjaan_row);
            tgl_mulai = (TextView) view.findViewById(R.id.tgl_mulai_row);
            tgl_selesai = (TextView) view.findViewById(R.id.tgl_selesai_row);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ViewResponKeluhanAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ViewResponKeluhanAdapter.ClickListener clickListener) {
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
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public void clear() {
        int size = this.listResponKeluhanArrayListView.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listResponKeluhanArrayListView.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void removeItem(int position){
        listResponKeluhanArrayListView.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listResponKeluhanArrayListView.size());
    }
}
