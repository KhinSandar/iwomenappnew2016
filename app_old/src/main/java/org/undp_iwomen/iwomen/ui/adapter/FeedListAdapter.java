package org.undp_iwomen.iwomen.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.FeedItem;
import org.undp_iwomen.iwomen.ui.widget.ProfilePictureView;
import org.undp_iwomen.iwomen.ui.widget.ResizableImageView;

import java.util.List;


public class FeedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    Context context;

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.stories_list_item_simple, null);



        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        ProfilePictureView profilePic = (ProfilePictureView) convertView
                .findViewById(R.id.profilePic);
        ResizableImageView profilePic_resizable = (ResizableImageView) convertView
                .findViewById(R.id.profilePic_resizable);
        ResizableImageView feedImageView = (ResizableImageView) convertView
                .findViewById(R.id.feedImage1);
        ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar_feed);
        FeedItem item = feedItems.get(position);
        /*

        name.setText(item.getName());

        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }*/

        // user profile pic
        //profilePic.setImageUrl(item.getProfilePic(), imageLoader);
        try {
            profilePic.setVisibility(View.GONE);
            profilePic_resizable.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(item.getPost_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_community)
                    .into(profilePic_resizable, new ImageLoadedCallback(progressBar) {
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

        // Feed image
        if (item.getPost_img_path() != null) {
            try {
                feedImageView.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(item.getPost_img_path()) //"http://cheapandcheerfulshopper.com/wp-content/uploads/2013/08/shopping1257549438_1370386595.jpg" //deal.photo1
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_community)
                        .into(feedImageView, new ImageLoadedCallback(progressBar) {
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
            feedImageView.setVisibility(View.GONE);
        }
        /*if (item.getImge() != null) {
            feedImageView.setImageUrl(item.getImge(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }*/

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


}
