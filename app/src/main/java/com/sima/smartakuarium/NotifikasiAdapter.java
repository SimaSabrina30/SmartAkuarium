package com.sima.smartakuarium;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private List<NotifikasiItem> dataList;

    public NotifikasiAdapter(List<NotifikasiItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifikasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotifikasiItem item = dataList.get(position);
        holder.tvPesan.setText(item.getPesan());
        holder.tvWaktu.setText(item.getWaktu());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPesan, tvWaktu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPesan = itemView.findViewById(R.id.tvPesan);
            tvWaktu = itemView.findViewById(R.id.tvWaktu);
        }
    }
}
