package moe.feng.luspeed.ui.adapter;

import android.support.annotation.DrawableRes;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import moe.feng.luspeed.R;

public class CircledItemAdapter extends WearableListView.Adapter {

	private ArrayList<Item> items;

	public CircledItemAdapter(ArrayList<Item> items) {
		this.items = items;
	}

	@Override
	public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			default:
				return new ItemViewHolder(View.inflate(parent.getContext(), R.layout.list_item_circled,null));
		}
	}

	@Override
	public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
		if (holder instanceof ItemViewHolder) {
			ItemViewHolder mHolder = (ItemViewHolder) holder;
			if (getItem(position).drawableId != -1) {
				mHolder.imageView.setImageResource(getItem(position).drawableId);
			}
			mHolder.setTitle(getItem(position).title);
			mHolder.setSmallText(getItem(position).content);
		}
	}

	public Item getItem(int position) {
		return items.get(position);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public class Item {

		public int drawableId = -1;
		public String title = null, content = null;

		public Item(String title, String content, @DrawableRes int drawableId) {
			this.title = title;
			this.content = content;
			this.drawableId = drawableId;
		}

	}

	public class ItemViewHolder extends WearableListView.ViewHolder {

		public CircledImageView imageView;
		public TextView title, smallText;

		public ItemViewHolder(View itemView) {
			super(itemView);
			imageView = (CircledImageView) itemView.findViewById(R.id.image_view);
			title = (TextView) itemView.findViewById(R.id.title);
			smallText = (TextView) itemView.findViewById(R.id.content);
		}

		public void setTitle(String text) {
			if (title != null) {
				title.setText(text);
			}
		}

		public void setSmallText(String text) {
			if (smallText != null) {
				smallText.setText(text);
				smallText.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
			}
		}

	}

}
