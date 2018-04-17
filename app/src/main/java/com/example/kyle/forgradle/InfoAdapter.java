package com.example.kyle.forgradle;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nick.yang on 2017/7/10.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoHolder> {
    private List<Info> infos;
    private Context context;

    public InfoAdapter(List<Info> infos, Context context) {
        this.context = context;
        this.infos = infos;
    }

    static class InfoHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView info1TV;
        TextView info2TV;

        public InfoHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardview);
            info1TV = (TextView) itemView.findViewById(R.id.text1);
            info2TV = (TextView) itemView.findViewById(R.id.text2);
        }
    }


    @Override
    public InfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        InfoHolder infoHolder = new InfoHolder(view);
        return infoHolder;
    }

    @Override
    public void onBindViewHolder(InfoHolder holder, int position) {
        final int p = position;

        holder.info1TV.setText(infos.get(p).info1);
        holder.info2TV.setText(infos.get(p).info2);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "click item: " + p, Toast.LENGTH_SHORT).show();
                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                Intent intent = new Intent();
                String action;
                switch (p) {
                    case 1:
                        action = Info.INFO_ACTION_2;
                        break;
                    case 2:
                        action = Info.INFO_ACTION_3;
                        break;
                    case 3:
                        action = Info.INFO_ACTION_4;
                        break;
                    case 4:
                        action = Info.INFO_ACTION_5;
                        break;
                    case 5:
                        action = Info.INFO_ACTION_6;
                        break;
                    case 6:
                        action = Info.INFO_ACTION_7;
                        break;
                    case 7:
                        action = Info.INFO_ACTION_8;
                        break;
                    case 8:
                        action = Info.INFO_ACTION_9;
                        break;
                    default:
                        // 0
                        action = Info.INFO_ACTION_1;
                        break;
                }
                intent.setAction(action);
                lbm.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    public void addInfo(Info info, int position) {
        infos.add(info);
        notifyItemInserted(position);
    }

    public void removeInfo(Info info, int position) {
        infos.remove(position);
        notifyItemRemoved(position);
    }
}
