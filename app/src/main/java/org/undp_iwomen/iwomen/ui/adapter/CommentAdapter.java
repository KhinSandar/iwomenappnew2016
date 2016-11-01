package org.undp_iwomen.iwomen.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algo.hha.emojiicon.EmojiconTextView;
import com.makeramen.RoundedImageView;
import com.smk.model.CommentItem;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;

import java.util.List;

/**
 * Created by khinsandar on 8/13/15.
 */
public class CommentAdapter extends BaseAdapter {

    List<com.smk.model.CommentItem> datalist;
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
    public CommentItem getItem(int i) {
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
            vh.pictureView = (RoundedImageView) view.findViewById(R.id.custom_comment_profilepicture);
            vh.tv_name = (TextView) view.findViewById(R.id.custom_comment_name);
            vh.tv_message = (EmojiconTextView) view.findViewById(R.id.custom_comment_message);
            vh.tv_timestamp = (TextView) view.findViewById(R.id.custom_comment_timestamp);
            vh.progressBar = (ProgressBar)view.findViewById(R.id.custom_comment_progressBar);

            vh.ly_sticker = (FrameLayout)view.findViewById(R.id.custom_comment_sticker_ly);
            vh.stickerView = (RoundedImageView)view.findViewById(R.id.custom_comment_sticker_img);
            vh.stickerProgressBar = (ProgressBar)view.findViewById(R.id.custom_comment_sticker_progressBar);
            view.setTag(vh);

        }else{
            vh = (ViewHolder) view.getTag();
        }
        //Log.e("adapter==","==>"+ datalist.get(i).get_profile_picture());
        if (datalist.get(i).getUserImgPath() != null && !datalist.get(i).getUserImgPath().isEmpty() && datalist.get(i).getUserImgPath() != "" && datalist.get(i).getUserImgPath() != "null") {

            //Log.e("adapter==","=if=>"+ datalist.get(i).get_profile_picture());
            try {

                Picasso.with(mActivity)
                        .load(datalist.get(i).getUserImgPath()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(vh.pictureView, new ImageLoadedCallback(vh.progressBar) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        } else {
            //Log.e("adapter==","=else=>"+ datalist.get(i).get_profile_picture());
            vh.progressBar.setVisibility(View.GONE);
        }


        //Log.e("adapter==","==>"+ datalist.get(i).get_profile_picture());
        if (datalist.get(i).getStickerImgPath() != null && !datalist.get(i).getStickerImgPath().isEmpty() && datalist.get(i).getStickerImgPath() != "" && datalist.get(i).getStickerImgPath() != "null") {

            //Log.e("adapter==","=if=>"+ datalist.get(i).get_profile_picture());
            try {

                Picasso.with(mActivity)
                        .load(datalist.get(i).getStickerImgPath()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(vh.stickerView, new ImageLoadedCallback(vh.stickerProgressBar) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
        } else {
            //Log.e("adapter==","=else=>"+ datalist.get(i).get_profile_picture());
            vh.stickerProgressBar.setVisibility(View.GONE);
            vh.ly_sticker.setVisibility(View.GONE);

        }

        vh.tv_name.setText(datalist.get(i).getUserName());
        vh.tv_message.setText(datalist.get(i).getCommentContents());
        vh.tv_timestamp.setText(datalist.get(i).getHumanCreatedAt());

        return view;
    }

    public class ViewHolder{
        RoundedImageView pictureView,stickerView;
        TextView tv_name , tv_timestamp;
        EmojiconTextView tv_message;

        ProgressBar progressBar,stickerProgressBar;
        FrameLayout ly_sticker;
    }

    private class ImageLoadedCallback implements com.squareup.picasso.Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progBar) {
            progressBar = progBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }
    }

}
