package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.smk.model.IWomenPost;
import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ProfilePictureView;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by SMK on 2/19/2016.
 */
public class TLGUserPostRecentListAdapter extends BaseAdapter {

    private final List<IWomenPost> list;
    private final LayoutInflater mInflater;
    private Context mContext;
    private String mstr_lang;
    private ArrayList<IWomenPost> arraylist;
    private SharedPreferences mSharedPreferencesUserInfo;
    String userID;


    public TLGUserPostRecentListAdapter(Context ctx, List<IWomenPost> list, String typeFaceName) {
        mInflater = LayoutInflater.from(ctx);
        this.mContext = ctx;
        this.list = list;
        mstr_lang = typeFaceName;
        this.arraylist = new ArrayList<IWomenPost>();
        this.arraylist.addAll(list);

        mSharedPreferencesUserInfo = mContext.getSharedPreferences(CommonConfig.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        userID = mSharedPreferencesUserInfo.getString(CommonConfig.USER_ID, null);


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public IWomenPost getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.stories_list_item, null);

            holder.mPostTile = (CustomTextView) convertView.findViewById(R.id.txtPostTitle);
            holder.post_content = (CustomTextView) convertView.findViewById(R.id.txtContent);
            holder.post_content_user_name = (CustomTextView) convertView.findViewById(R.id.name);
            holder.post_timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            holder.profile = (RoundedImageView) convertView.findViewById(R.id.profilePic_rounded);
            holder.feed_item_progressBar = (ProgressBar) convertView.findViewById(R.id.feed_item_progressBar);
            holder.profile_item_progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar_profile_item);
            holder.postIMg = (ResizableImageView) convertView.findViewById(R.id.postImg);
            holder.profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.profilePic);
            holder.post_deleted = (CustomTextView)convertView.findViewById(R.id.txtPostDeleted);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        IWomenPost item = getItem(position);

        holder.profile.setAdjustViewBounds(true);
        holder.profile.setScaleType(ImageView.ScaleType.CENTER_CROP);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date timedate = format.parse(item.getPostUploadedDate());

            String dateformat = sdf.format(timedate);//M08 11, 2016
            String regex = "[0-9]+";

            String prefixDate = dateformat.substring(1, 3);
            if (prefixDate.matches(regex)) {//if equal with number
                if (prefixDate.equals("01")) {
                    holder.post_timestamp.setText("Jan" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("02")) {
                    holder.post_timestamp.setText("Feb" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("03")) {
                    holder.post_timestamp.setText("Mar" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("04")) {
                    holder.post_timestamp.setText("Apr" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("05")) {
                    holder.post_timestamp.setText("May" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("06")) {
                    holder.post_timestamp.setText("Jun" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("07")) {
                    holder.post_timestamp.setText("Jul" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("08")) {
                    holder.post_timestamp.setText("Aug" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("09")) {
                    holder.post_timestamp.setText("Sep" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("10")) {
                    holder.post_timestamp.setText("Oct" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("11")) {
                    holder.post_timestamp.setText("Nov" + dateformat.substring(3, dateformat.length()));
                } else if (prefixDate.equals("12")) {
                    holder.post_timestamp.setText("Dec" + dateformat.substring(3, dateformat.length()));
                }

            } else {
                //Log.e("Date==>prefix not match" , prefixDate);
                holder.post_timestamp.setText(dateformat);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


        //if(holder.post_timestamp.getText().)
        //holder.mPostTile.setText(item.getPost_title());
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            holder.mPostTile.setText(item.getTitle());
            holder.post_content.setText(item.getContent());
            holder.post_content_user_name.setText(item.getPostUploadName());
            holder.mPostTile.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            holder.post_content.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));


        } else {//FOR ALL MM FONTS
            holder.mPostTile.setText(item.getTitleMm());
            holder.post_content.setText(item.getContentMm());
            holder.post_content_user_name.setText(item.getPostUploadNameMM());

        }

        Log.e("<<Talk Together >>","==usrID=>" + userID+ "/"+ item.getUserId());
        if(item.getUserId().toString().equals(userID)){
            holder.post_deleted.setVisibility(View.VISIBLE);

        }else{
            holder.post_deleted.setVisibility(View.GONE);
        }


        if (item.getPostUploadUserImgPath() != null && !item.getPostUploadUserImgPath().isEmpty()) {

            try {
                holder.profilePictureView.setVisibility(View.GONE);
                holder.profile.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(item.getPostUploadUserImgPath()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(holder.profile, new ImageLoadedCallback(holder.profile_item_progressBar) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError() {
                                super.onError();
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
                holder.profile_item_progressBar.setVisibility(View.GONE);
            }
        } else {
            holder.profilePictureView.setVisibility(View.GONE);
            holder.profile.setImageResource(R.drawable.blank_profile);
            holder.profile_item_progressBar.setVisibility(View.GONE);
        }

        //// Feed image
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            try {
                holder.postIMg.setVisibility(View.VISIBLE);
                holder.feed_item_progressBar.setVisibility(View.VISIBLE);

                Picasso.with(mContext)
                        .load(item.getImage()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(holder.postIMg, new ImageLoadedCallback(holder.feed_item_progressBar) {
                            @Override
                            public void onSuccess() {
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                } else {
                                    this.progressBar.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onError() {
                                super.onError();
                                if (this.progressBar != null) {
                                    this.progressBar.setVisibility(View.GONE);
                                }
                            }

                        });
            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
                holder.feed_item_progressBar.setVisibility(View.GONE);
            }
        } else {
            holder.postIMg.setVisibility(View.GONE);
            holder.feed_item_progressBar.setVisibility(View.GONE);
        }


        return convertView;
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

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }


    static class ViewHolder {
        CustomTextView mPostTile;
        CustomTextView post_content;
        TextView post_like;
        TextView post_img_path;
        TextView post_content_type;
        TextView post_content_user_id;
        CustomTextView post_content_user_name;
        CustomTextView post_deleted;
        TextView post_content_user_img_path;
        TextView post_timestamp;
        ProgressBar feed_item_progressBar;
        ProgressBar profile_item_progressBar;
        ProfilePictureView profilePictureView;
        RoundedImageView profile;
        ResizableImageView postIMg;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (IWomenPost fi : arraylist) {
                if (fi.getTitle().toLowerCase(Locale.getDefault()).contains(charText) || fi.getTitleMm().toLowerCase(Locale.getDefault()).contains(charText) || fi.getPostUploadName().toLowerCase(Locale.getDefault()).contains(charText) || fi.getContent().toLowerCase(Locale.getDefault()).contains(charText) || fi.getContentMm().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(fi);
                }
            }
        }
        notifyDataSetChanged();
    }
}
