package com.mobile.rsupsanglah.simrsmobile.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mobile.rsupsanglah.simrsmobile.R;
import com.mobile.rsupsanglah.simrsmobile.activity.model.listResponKeluhan;

import java.util.ArrayList;

public class listResponKeluhanAdapter extends RecyclerView.Adapter<listResponKeluhanAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<listResponKeluhan> listViewResponKeluhanAdapterArrayList;
    private ArrayList<listResponKeluhan> list;
    private listAdapterListener listener;

    public listResponKeluhanAdapter(Context context, ArrayList<listResponKeluhan> listViewResponKeluhanAdapterArrayList, listAdapterListener listener ) {
        this.context = context;
        this.listViewResponKeluhanAdapterArrayList = listViewResponKeluhanAdapterArrayList;
        this.list = listViewResponKeluhanAdapterArrayList;
        this.listener = listener;
    }

    @Override
    public listResponKeluhanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_respon_keluhan_row, parent, false);
        return new listResponKeluhanAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(listResponKeluhanAdapter.ViewHolder holder, int position) {
        final listResponKeluhan listResponKeluhan = listViewResponKeluhanAdapterArrayList.get(position);
        if (String.valueOf(listResponKeluhan.getKeluhan()).length() > 32) {
            String data = String.valueOf(listResponKeluhan.getKeluhan()).substring(0, 30)+ "...";
            holder.keluhan.setText(data);
        } else {
            holder.keluhan.setText(String.valueOf(listResponKeluhan.getKeluhan()));
        }
        holder.jenis_order.setText(String.valueOf(listResponKeluhan.getJenisOrder()));
        holder.pelapor.setText(String.valueOf(listResponKeluhan.getPelapor()));
        holder.nomor_order.setText(String.valueOf(listResponKeluhan.getNoOrder()));
    }

    @Override
    public int getItemCount() {
        return listViewResponKeluhanAdapterArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nomor_order, keluhan, jenis_order, pelapor;

        public ViewHolder(View view) {
            super(view);
            keluhan = (TextView) view.findViewById(R.id.nama_keluhan);
            jenis_order = (TextView) view.findViewById(R.id.jenis_order);
            pelapor = (TextView) view.findViewById(R.id.nama_pelapor);
            nomor_order = (TextView) view.findViewById(R.id.nomor_order);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(listViewResponKeluhanAdapterArrayList.get(getAdapterPosition()));
                }
            });
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listViewResponKeluhanAdapterArrayList = list;
                } else {
                    ArrayList<listResponKeluhan> filteredList = new ArrayList<>();
                    for (listResponKeluhan row : list) {
                        if (row.getKeluhan().toLowerCase().contains(charString.toLowerCase()) || row.getId().contains(charSequence)
                                || row.getNoOrder().contains(charSequence) || row.getJenisOrder().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    listViewResponKeluhanAdapterArrayList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listViewResponKeluhanAdapterArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listViewResponKeluhanAdapterArrayList = (ArrayList<listResponKeluhan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface listAdapterListener {
        void onContactSelected(listResponKeluhan listResponKeluhan);
    }

}

