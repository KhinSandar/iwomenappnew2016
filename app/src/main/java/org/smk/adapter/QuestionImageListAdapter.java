package org.smk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.smk.model.Option;
import org.undp_iwomen.iwomen.R;

import java.util.List;

public class QuestionImageListAdapter extends BaseAdapter {

	private final LayoutInflater mInflater;
	private Context ctx;
	private List<Option> lists;

	public QuestionImageListAdapter(Context ctx, List<Option> list){
		this.ctx = ctx;
		this.lists = list;
		mInflater = LayoutInflater.from(ctx);
	}


	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Option getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null){

			holder = new ViewHolder();

			convertView = mInflater.inflate(R.layout.list_item_image, null);

			holder.img_question = (ImageView) convertView.findViewById(R.id.img_question);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Picasso.with(ctx).load(getItem(position).getOption()).into(holder.img_question);

		return convertView;
	}

	static class ViewHolder{
		ImageView img_question;
	}
}
