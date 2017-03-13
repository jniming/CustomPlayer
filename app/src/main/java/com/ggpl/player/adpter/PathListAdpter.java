package com.ggpl.player.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggpl.player.R;

import java.io.File;
import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class PathListAdpter extends RecyclerView.Adapter<PathListAdpter.ItemView> {

    private List<File> list;
    private Context mContext;
    private PathListAdpter.ItemClick itemClick;

    public PathListAdpter(List<File> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.filepath, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        final File bean = list.get(position);
        holder.name.setText(bean.getName());
        if (bean.isDirectory()) {

            holder.num.setVisibility(View.GONE);
            holder.img.setImageResource(R.mipmap.icon_file);
        } else {
            holder.num.setVisibility(View.VISIBLE);
            holder.num.setText(getSize(bean.length()));
            holder.img.setImageResource(R.mipmap.icon_wenj);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    itemClick.itemClickListener(bean);
            }
        });

    }

    public String getSize(long s) {
        return s / 1024 / 1024 + "M";
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {
        private TextView name, num;
        private ImageView img;


        public ItemView(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.dir_img);
            name = (TextView) itemView.findViewById(R.id.dirname);
            num = (TextView) itemView.findViewById(R.id.dirnum);

        }
    }

    public void setItemClick(PathListAdpter.ItemClick itemClick) {

        this.itemClick = itemClick;
    }

    public interface ItemClick {

        void itemClickListener(File position);


    }
}
