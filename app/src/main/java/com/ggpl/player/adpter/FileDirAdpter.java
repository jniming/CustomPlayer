package com.ggpl.player.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggpl.player.R;
import com.ggpl.player.common.DebugLog;
import com.ggpl.player.model.bean.FileDirBean;
import com.ggpl.player.model.entitiy.FileDirEntity;

import java.util.List;

/**
 * Created by zhangxiaoming on 2017/2/16.
 */

public class FileDirAdpter extends RecyclerView.Adapter<FileDirAdpter.ItemView> {

    private List<FileDirEntity> list;
    private Context mContext;
    private FileDirAdpter.ItemClick itemClick;

    public FileDirAdpter(List<FileDirEntity> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemView(LayoutInflater.from(mContext).inflate(R.layout.filedir_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        final FileDirBean bean=list.get(position).getDirBean();
        holder.name.setText(bean.getDirname()+"");

        holder.num.setText(list.get(position).getVideoList().size()+mContext.getResources().getString(R.string.video_num_string));

        holder.box.setChecked(Integer.valueOf(bean.getIshiden()) == 1);

        holder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          DebugLog.d("点击了");
          bean.setIshiden(isChecked?"1":"0");
          itemClick.itemClickListener(bean);
      }
     });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ItemView extends RecyclerView.ViewHolder {
        private TextView name, num;
        private ImageView img;
        private CheckBox box;

        public ItemView(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.dir_img);
            name = (TextView) itemView.findViewById(R.id.dirname);
            num = (TextView) itemView.findViewById(R.id.dirnum);
            box = (CheckBox) itemView.findViewById(R.id.checkbox);

        }
    }

    public void setItemClick(FileDirAdpter.ItemClick itemClick) {

        this.itemClick = itemClick;
    }

    public interface ItemClick {

        void itemClickListener(FileDirBean position);


    }
}
