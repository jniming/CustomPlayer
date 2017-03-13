package com.ggpl.player.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggpl.player.R;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.util.ThumbLoader;

import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/22.
 */

public class PlayerPopAdpter extends RecyclerView.Adapter<PlayerPopAdpter.ItemView> {
    private List<Videobean> list;
    private Context mcontext;
    private PlayerPopAdpter.ItemClick itemClick;

    public PlayerPopAdpter(List<Videobean> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerPopAdpter.ItemView(LayoutInflater.from(mcontext).inflate(R.layout.player_pop_item, parent,
                false));

    }

    @Override
    public void onBindViewHolder(ItemView holder, final int position) {

        final Videobean ben = list.get(position);
        holder.name.setText("" + ben.getTitle());
        ThumbLoader.getIntentce(mcontext).showThumbByAsynctack(ben.getPath(), holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClickListener(ben,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView img;


        public ItemView(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_img);
            name = (TextView) itemView.findViewById(R.id.item_text);


        }
    }
    public void setItemClick(PlayerPopAdpter.ItemClick itemClick) {

        this.itemClick = itemClick;
    }

    public interface ItemClick {

        void itemClickListener(Videobean position,int index);


    }


}
