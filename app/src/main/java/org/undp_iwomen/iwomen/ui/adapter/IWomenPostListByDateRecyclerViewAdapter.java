package org.undp_iwomen.iwomen.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.smk.model.IWomenPost;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.model.MyTypeFace;
import org.undp_iwomen.iwomen.ui.widget.CustomTextView;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IWomenPostListByDateRecyclerViewAdapter extends RecyclerView.Adapter<IWomenPostListByDateRecyclerViewAdapter.NamesViewHolder> {

    private Context mContext;
    private List<IWomenPost> feedItems;
    private ArrayList<IWomenPost> arraylist;
    String mstr_lang;

    public IWomenPostListByDateRecyclerViewAdapter(Context context, List<IWomenPost> feedItems, String typeFaceName) {
        mContext = context;
        this.feedItems = feedItems;

        this.arraylist = new ArrayList<IWomenPost>();
        this.arraylist.addAll(feedItems);
        mstr_lang = typeFaceName;
        //randomizeCatNames();
    }

    /*public void randomizeCatNames() {
        mCatNames = Arrays.asList(getCatNamesResource());
        Collections.shuffle(mCatNames);
    }
    */

    public class NamesViewHolder extends RecyclerView.ViewHolder {
        CustomTextView mPostTile;

        CustomTextView post_content;
        TextView post_like;
        TextView post_img_path;
        TextView post_content_type;
        TextView post_content_user_id;
        CustomTextView post_content_user_name;
        TextView post_content_user_img_path;
        TextView post_timestamp;
        private ProgressBar feed_item_progressBar;
        private ProgressBar profile_item_progressBar;
        private org.undp_iwomen.iwomen.ui.widget.ProfilePictureView profilePictureView;
        private RoundedImageView profile;
        private ResizableImageView postIMg;

        public NamesViewHolder(View itemView) {
            super(itemView);
            mPostTile = (CustomTextView) itemView.findViewById(R.id.txtPostTitle);
            post_content = (CustomTextView) itemView.findViewById(R.id.txtContent);

            post_content_user_name = (CustomTextView) itemView.findViewById(R.id.name);

            post_timestamp = (TextView) itemView.findViewById(R.id.timestamp);

            profile = (RoundedImageView) itemView.findViewById(R.id.profilePic_rounded);
            feed_item_progressBar = (ProgressBar) itemView.findViewById(R.id.feed_item_progressBar);
            profile_item_progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar_profile_item);

            postIMg = (ResizableImageView) itemView.findViewById(R.id.postImg);
            profilePictureView = (org.undp_iwomen.iwomen.ui.widget.ProfilePictureView) itemView.findViewById(R.id.profilePic);
        }


    }

    /*private String[] getCatNamesResource() {
        return mContext.getResources().getStringArray(R.array.lostlist);
    }*/

    @Override
    public NamesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflatedView = LayoutInflater.from(mContext).inflate(R.layout.stories_list_item, viewGroup, false);
        return new NamesViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(NamesViewHolder viewHolder, int i) {

        IWomenPost item = feedItems.get(i);


        viewHolder.profile.setAdjustViewBounds(true);
        viewHolder.profile.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //viewHolder.mPostTile.setText(item.getPost_title());
        if (mstr_lang.equals(org.undp_iwomen.iwomen.utils.Utils.ENG_LANG)) {
            viewHolder.mPostTile.setText(item.getTitle());
            viewHolder.post_content.setText(item.getContent());
            viewHolder.post_content_user_name.setText(item.getPostUploadName());

            viewHolder.mPostTile.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));
            viewHolder.post_content.setTypeface(MyTypeFace.get(mContext, MyTypeFace.NORMAL));

        } else  {//FOR ALL MM FONTS
            viewHolder.mPostTile.setText(item.getTitleMm());
            viewHolder.post_content.setText(item.getContentMm());
            viewHolder.post_content_user_name.setText(item.getPostUploadName());


        }


        /*SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        ParsePosition pp = new ParsePosition(0);
        try {
            Date timedate = ISO8601Utils.parse(item.getCreatedAt().toString(), pp);

            viewHolder.post_timestamp.setText(sdf.format(timedate));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        //2016-02-05 17:56:36 (SMK SERVER)


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        //2015-09-16T13:46:34.875Z
        //Sun Jun 22 18:32:00 GMT+06:30 2014
        //Log.e("Stories Post Adapter==","Date===>" + item.getCreated_at());
        try {
            Date timedate = format.parse(item.getPostUploadedDate());
            viewHolder.post_timestamp.setText(sdf.format(timedate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

       
        if (item.getPostUploadUserImgPath() != null && !item.getPostUploadUserImgPath().isEmpty()) {
            try {
                viewHolder.profilePictureView.setVisibility(View.GONE);
                viewHolder.profile.setVisibility(View.VISIBLE);
                Picasso.with(mContext)
                        .load(item.getPostUploadUserImgPath()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.blank_profile)
                        .error(R.drawable.blank_profile)
                        .into(viewHolder.profile, new ImageLoadedCallback(viewHolder.profile_item_progressBar) {
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
            viewHolder.profilePictureView.setVisibility(View.GONE);
            viewHolder.profile.setImageResource(R.drawable.blank_profile);
            viewHolder.profile_item_progressBar.setVisibility(View.GONE);
        }

        //// Feed image
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            try {
                viewHolder.postIMg.setVisibility(View.VISIBLE);
                viewHolder.feed_item_progressBar.setVisibility(View.VISIBLE);

                Picasso.with(mContext)
                        .load(item.getImage()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.place_holder)
                        .into(viewHolder.postIMg, new ImageLoadedCallback(viewHolder.feed_item_progressBar) {
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
            viewHolder.postIMg.setVisibility(View.GONE);
            viewHolder.feed_item_progressBar.setVisibility(View.GONE);
        }


    }



    /*@Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, PostDetailActivity.class);

        int pos = (int) v.getTag();
        intent.putExtra("user_id", feedItems.get(pos).getPost_obj_id());

        //intent.putExtra("ImgUrl", mImgurl.get(getPosition()));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();

    }*/


    public Object getItem(int location) {
        return feedItems.get(location);
    }


    @Override
    public int getItemCount() {
        return feedItems.size();
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

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        feedItems.clear();
        if (charText.length() == 0) {
            feedItems.addAll(arraylist);
        } else {
            for (IWomenPost fi : arraylist) {
                if (fi.getTitle().toLowerCase(Locale.getDefault()).contains(charText) || fi.getTitleMm().toLowerCase(Locale.getDefault()).contains(charText)|| fi.getPostUploadName().toLowerCase(Locale.getDefault()).contains(charText)|| fi.getContent().toLowerCase(Locale.getDefault()).contains(charText) || fi.getContentMm().toLowerCase(Locale.getDefault()).contains(charText)) {
                    feedItems.add(fi);
                }
            }
        }
        notifyDataSetChanged();
    }

}
