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
import com.mobile.rsupsanglah.simrsmobile.activity.model.listInputBahan;

import java.util.ArrayList;

public class InputBahanAdapter extends RecyclerView.Adapter<InputBahanAdapter.ViewHolder> {

    private Context context;
    private ArrayList<listInputBahan> listInputBahanArrayList;

    public InputBahanAdapter(Context context, ArrayList<listInputBahan> listInputBahanArrayList) {
        this.context = context;
        this.listInputBahanArrayList = listInputBahanArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_input_bahan_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        listInputBahan listInputBahan = listInputBahanArrayList.get(position);
        holder.nama_history_input_bahan.setText(String.valueOf(listInputBahan.getNama_bahan()));
        holder.jml_bahan.setText(String.valueOf(listInputBahan.getJumlah_bahan()));
        holder.satuan_bahan.setText(String.valueOf(listInputBahan.getSatuanBahan()));
    }

    @Override
    public int getItemCount() {
        return listInputBahanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_history_input_bahan, jml_bahan, satuan_bahan;

        public ViewHolder(View view) {
            super(view);
            nama_history_input_bahan = (TextView) view.findViewById(R.id.nama_history_input_bahan);
            jml_bahan = (TextView) view.findViewById(R.id.jumlah_bahan_row);
            satuan_bahan = (TextView) view.findViewById(R.id.satuan_bahan_row);

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private InputBahanAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final InputBahanAdapter.ClickListener clickListener) {
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

    public void clear() {
        int size = this.listInputBahanArrayList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listInputBahanArrayList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void removeItem(int position){
        listInputBahanArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listInputBahanArrayList.size());
    }
}
