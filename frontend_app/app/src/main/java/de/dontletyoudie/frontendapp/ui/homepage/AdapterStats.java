package de.dontletyoudie.frontendapp.ui.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.HomeFragment;

public class AdapterStats extends RecyclerView.Adapter<AdapterStats.ViewHolder>{

    Context context;
    private HomeFragment homeFragment;

    public AdapterStats(Context context, HomeFragment homeFragment) {
        this.context = context;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public AdapterStats.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterStats.ViewHolder holder, int position) {
        holder.tvStatName.setText("Hygiene");
        holder.progressBar.setProgress(5);
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatName;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatName = itemView.findViewById((R.id.tv_home_statTitle));
            progressBar = itemView.findViewById(R.id.pb_home_progressBar);
        }
    }
}
