package com.moutamid.streamingapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.streamingapp.R;
import com.moutamid.streamingapp.dialog.VideoPlayerDialog;
import com.moutamid.streamingapp.models.ChannelsModel;
import com.moutamid.streamingapp.models.StreamLinksModel;

import java.util.ArrayList;

public class StreamLinksAdapter extends RecyclerView.Adapter<StreamLinksAdapter.LinkVH> {
    Context context;
    ArrayList<StreamLinksModel> list;
    Dialog dialog;
    ChannelsModel channelsModel;

    public StreamLinksAdapter(Context context, ArrayList<StreamLinksModel> list, Dialog dialog, ChannelsModel channelsModel) {
        this.context = context;
        this.list = list;
        this.dialog = dialog;
        this.channelsModel = channelsModel;
    }

    @NonNull
    @Override
    public LinkVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.links, parent, false);
        return new LinkVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkVH holder, int position) {
        StreamLinksModel model = list.get(holder.getAdapterPosition());
        holder.txt.setText(model.getName());

        holder.itemView.setOnClickListener(v -> {int idx = Stash.getInt("buttonIDX", 0);
            if (idx == 0) {
                videoPlayerDialog(list.get(holder.getAdapterPosition()));
            }
            dialog.dismiss();
        });
    }

    private void videoPlayerDialog(StreamLinksModel model) {
        VideoPlayerDialog vd = new VideoPlayerDialog(context, model, channelsModel);
        vd.showStream();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LinkVH extends RecyclerView.ViewHolder{
        Button txt;

        public LinkVH(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.linkText);
        }
    }

}
