package com.ggpl.player.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggpl.player.R;
import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.bean.ModuleBean;
import com.ggpl.player.model.entitiy.FileDirEntity;
import com.ggpl.player.util.ThumbLoader;
import com.ggpl.player.util.UiScreen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class MainDirListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FileDirEntity> list;
    private Context mContext;
    private MainDirListAdpter.ItemClick itemClick;

    private static int FILE_TYPE = 0;
    private static int LIGHT_TYPE = 1;

    public MainDirListAdpter(List<FileDirEntity> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LIGHT_TYPE) {
            return new LightItemView(LayoutInflater.from(mContext).inflate(R.layout.light_item, parent, false));
        } else if (viewType == FILE_TYPE) {
            return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.main_filedir_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final FileDirEntity entity = list.get(position);
        if (holder instanceof ItemView) {


            final FileDirBean bean = entity.getDirBean();

            ((ItemView) holder).name.setText(bean.getDirname() + "");

            ((ItemView) holder).num.setText(entity.getVideoList().size() + mContext.getString(R.string
                    .video_num_string));

            ((ItemView) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.itemClickListener(entity);
                }
            });

        } else if (holder instanceof LightItemView) {
            ModuleBean moduleBean = entity.getModuleBean();
            ((LightItemView) holder).sort.setText(moduleBean.getDesc());
            ((LightItemView) holder).name.setText(moduleBean.getName());


            Bitmap bt = ThumbLoader.toRoundCornerImage(getLoacalBitmap(new File(moduleBean.getImgurl())), UiScreen.dip2px
                    (mContext, 6));

            ((LightItemView) holder).layout.setImageBitmap(bt);



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.itemClickListener(entity);
                }
            });


        }

    }



    @Override
    public int getItemViewType(int position) {
        int type = list.get(position).getType();
        return type;
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


    public class LightItemView extends RecyclerView.ViewHolder {

        private ImageView layout;
        private TextView name, sort;
private FrameLayout item_layout;
        public LightItemView(View itemView) {
            super(itemView);
            layout = (ImageView) itemView.findViewById(R.id.imgview);
            name = (TextView) itemView.findViewById(R.id.module_name);
            sort = (TextView) itemView.findViewById(R.id.module_desc);
            item_layout= (FrameLayout) itemView.findViewById(R.id.item_layout);
        }
    }


    public void setItemClick(MainDirListAdpter.ItemClick itemClick) {

        this.itemClick = itemClick;
    }

    public interface ItemClick {

        void itemClickListener(FileDirEntity position);


    }


    /**
     * 加载本地图片
     *
     * @param
     * @return
     */
    public Bitmap getLoacalBitmap(File file) {

        try {

            FileInputStream fis = new FileInputStream(file);

            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
