package com.ggpl.player.adpter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggpl.player.R;
import com.ggpl.player.manager.DataManager;
import com.ggpl.player.model.bean.Videobean;
import com.ggpl.player.util.PhoneInfo;
import com.ggpl.player.util.ThumbLoader;

import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/15.
 */

public class VideoListAdpter extends RecyclerView.Adapter<VideoListAdpter.ItemView> {

    private List<Videobean> list;
    private Context mContext;
    private ItemClick itemClick;

    public VideoListAdpter(List<Videobean> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }


    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.videolist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        final Videobean videobean = list.get(position);
        long ti=Long.valueOf(videobean.getTime());
        String wm=videobean.getWh();

//       Spanned title= Html.fromHtml(videobean.getTitle());

        if(wm.equals("0x0")){

          new  MyBobAsynctack(holder.screen,videobean.getPath());
        }else {
            holder.screen.setText(wm);
        }
        holder.title.setText(videobean.getTitle());
        holder.time.setText(DataManager.EcodeTime(ti));
        holder.size.setText(videobean.getSize());
        holder.imageView.setTag(videobean.getPath());

        ThumbLoader.getIntentce(mContext).showThumbByAsynctack(videobean.getPath(),holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClickListener(videobean);
            }
        });
    }
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ItemView extends RecyclerView.ViewHolder {
        private TextView title, time, size, screen;
        private ImageView imageView;

        public ItemView(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.item_time);
            title = (TextView) itemView.findViewById(R.id.item_title);
            size = (TextView) itemView.findViewById(R.id.item_size);
            screen = (TextView) itemView.findViewById(R.id.item_screen);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);


        }
    }

    public void setItemClick(ItemClick itemClick) {

        this.itemClick = itemClick;
    }

    public interface ItemClick {

        void itemClickListener(Videobean position);


    }

    class MyBobAsynctack extends AsyncTask<String, Void, String> {
        private TextView imgView;
        private String path;

        public MyBobAsynctack(TextView imageView, String path) {
            this.imgView = imageView;
            this.path = path;
        }

        @Override
        protected String doInBackground(String... params) {
            String bitmap = PhoneInfo.decodeThumbBitmapForFileToWidth(path);
            return bitmap;
        }

        @Override
        protected void onPostExecute(String bitmap) {
               imgView.setText(bitmap);
        }
    }

}


