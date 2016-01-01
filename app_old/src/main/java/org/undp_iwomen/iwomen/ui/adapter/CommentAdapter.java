package org.undp_iwomen.iwomen.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algo.hha.emojiicon.EmojiconTextView;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.CommentItem;
import org.undp_iwomen.iwomen.ui.widget.ProfilePictureView;

import java.util.List;

/**
 * Created by khinsandar on 8/13/15.
 */
public class CommentAdapter extends BaseAdapter {

    List<CommentItem> datalist;
    Activity mActivity;

    public CommentAdapter(Context context, List<CommentItem> dl){
        this.datalist = dl;
        mActivity = (Activity) context;
    }

    @Override
    public int getCount() {
        return this.datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return this.datalist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder vh;
        if(view == null){
            LayoutInflater inflater = mActivity.getLayoutInflater();
            view = inflater.inflate(R.layout.custom_comment_layout, viewGroup, false);

            vh = new ViewHolder();
            vh.pictureView = (ProfilePictureView) view.findViewById(R.id.custom_comment_profilepicture);
            vh.tv_name = (TextView) view.findViewById(R.id.custom_comment_name);
            vh.tv_message = (EmojiconTextView) view.findViewById(R.id.custom_comment_message);
            vh.tv_timestamp = (TextView) view.findViewById(R.id.custom_comment_timestamp);
            view.setTag(vh);

        }else{
            vh = (ViewHolder) view.getTag();
        }

        vh.tv_name.setText(datalist.get(i).get_user_name());
        vh.tv_message.setText(datalist.get(i).get_comment_message());
        vh.tv_timestamp.setText(datalist.get(i).get_timestamp());

        return view;
    }

    public class ViewHolder{
        ProfilePictureView pictureView;
        TextView tv_name , tv_timestamp;
        EmojiconTextView tv_message;
    }

}
