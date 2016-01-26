package com.smk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smk.application.CircleTransform;
import com.smk.model.Answer;
import com.smk.model.GroupUser;
import com.squareup.picasso.Picasso;

import org.undp_iwomen.iwomen.R;

import java.util.List;

public class GroupUserListAdapter extends BaseExpandableListAdapter {
	
	private Context ctx;
	private List<GroupUser> groups;
	private Callbacks mCallback;

	public GroupUserListAdapter(Context ctx, List<GroupUser> groupUsers){
		this.ctx = ctx;
		this.groups = groupUsers;
	}

	@Override
	public Answer getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return this.groups.get(arg0).getAnswer().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
	        boolean isLastChild, View convertView, ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {
	        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
	                  (Context.LAYOUT_INFLATER_SERVICE);
	        v = inflater.inflate(R.layout.competition_group_list_subanswer, parent, false);
	    }

	    TextView subAnswer = (TextView) v.findViewById(R.id.txt_competition_sub_answer);
	    subAnswer.setText(getChild(groupPosition, childPosition).getAnswer());

	    return v;

	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return this.groups.get(arg0).getAnswer().size();
	}

	@Override
	public GroupUser getGroup(int arg0) {
		// TODO Auto-generated method stub
		return groups.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.groups.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
	        View convertView, ViewGroup parent) {

	    View v = convertView;

	    if (v == null) {
	        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService
	                  (Context.LAYOUT_INFLATER_SERVICE);
	        v = inflater.inflate(R.layout.competition_group_list_answer, parent, false);
	    }
	    
	    
	    ImageView userPhoto = (ImageView) v.findViewById(R.id.img_competition_user_photo);
	    TextView userName = (TextView) v.findViewById(R.id.txt_competition_user_name);
	    TextView userStatus = (TextView) v.findViewById(R.id.txt_competition_status);
	    TextView userAnswer = (TextView) v.findViewById(R.id.txt_competition_answer);
	    ImageView userPhone = (ImageView) v.findViewById(R.id.img_competition_user_phone);
	    ImageView userViber = (ImageView) v.findViewById(R.id.img_competition_user_viber);
	    if(getGroup(groupPosition).getImageUrl() != null && getGroup(groupPosition).getImageUrl().length() > 0)
	    	Picasso.with(ctx).load(getGroup(groupPosition).getImageUrl()).placeholder(R.drawable.blank_profile).transform(new CircleTransform()).into(userPhoto);
	    userName.setText(getGroup(groupPosition).getUsername());
	    
	    if(getGroup(groupPosition).getInitAnswer() != null){
	    	/*SharedPreferences langRef = ctx.getSharedPreferences("mLanguage", Activity.MODE_PRIVATE);
			if(langRef.getString("lang","").equals("mm")){
				userAnswer.setText(getGroup(groupPosition).getInitAnswer().getAnswerMm());
			}else{
				userAnswer.setText(getGroup(groupPosition).getInitAnswer().getAnswer());
			}*/

			userAnswer.setText(getStringResource(R.string.competition_answer));
	    }else{
	    	userAnswer.setText(getStringResource(R.string.competition_not_answer));
	    }
	    
	    if(getGroup(groupPosition).getAnswer().size() >= 2){
	    	userPhoto.setBackgroundResource(R.drawable.competition_user_status_3);
	    	userStatus.setText(getStringResource(R.string.competition_finished));
	    }else{
	    	if(getGroup(groupPosition).getInitAnswer() != null){
	    		userPhoto.setBackgroundResource(R.drawable.competition_user_status_2);
	    		userStatus.setText((getGroup(groupPosition).getAnswer().size() + 1) +"/"+getStringResource(R.string.competition_finished));
	    	}
	    	else{
	    		userPhoto.setBackgroundResource(R.drawable.competition_user_status_1);
	    		userStatus.setText(getGroup(groupPosition).getAnswer().size() +"/"+getStringResource(R.string.competition_finished));
	    	}
	    }
	    
	    userPhone.setTag(getGroup(groupPosition).getPhone());
	    userPhone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phoneNo = (String) arg0.getTag();
				if(mCallback != null){
					mCallback.clickPhone(phoneNo);
				}
			}
		});
	    
	    userViber.setTag(getGroup(groupPosition).getPhone());
	    userViber.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phoneNo = (String) arg0.getTag();
				if(mCallback != null){
					mCallback.clickViber(phoneNo);
				}
			}
		});
	    

	    return v;

	}
	
	private String getStringResource(int R){
		return ctx.getResources().getString(R);
	}
	public void setOnCallbackListener(Callbacks callbacks){
		this.mCallback = callbacks;
	}
	public interface Callbacks{
		void clickPhone(String phoneNumber);
		void clickViber(String phoneNumber);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
