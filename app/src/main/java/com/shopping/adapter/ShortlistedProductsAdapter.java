package com.shopping.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shopping.url.parse.ProductData;
import com.squareup.picasso.Picasso;

import java.util.List;

import info.shopping.listviewfeed.R;

public class ShortlistedProductsAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ProductData> feedItems;

	public ShortlistedProductsAdapter(Activity activity, List<ProductData> feedItems) {
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
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.product_feed_item_compact1, null);

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		ImageView brand_image = (ImageView) convertView.findViewById(R.id.store_image);
		ImageView feedImageView = (ImageView) convertView.findViewById(R.id.feedImage1);

		ProductData item = feedItems.get(position);

		name.setText(item.getTitle());


		// Checking for null feed url
		if (item.getUrl() != null) {
            LinearLayout product_feed_item = (LinearLayout) convertView.findViewById(R.id.product_feed_item);
            product_feed_item.setTag(item.getUrl());
            //url.setVisibility(View.GONE);

			/*url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">" + item.getUrl() + "</a> "));
			// Making url clickable
			url.setMovementMethod(LinkMovementMethod.getInstance());
			url.setVisibility(View.VISIBLE);*/
		} else {
			// url is null, remove from the view
			//url.setVisibility(View.GONE);
		}

        statusMsg.setText(item.getDetails());


		// user Brand Image
        String brand_name = item.getBrand().toLowerCase();
        Log.d("Profile ", "item pf = " + brand_name);

        if(brand_name != null) {
            if (brand_name.contains("myntra"))
                brand_image.setImageResource(R.drawable.myntra_icon);
            else if(brand_name.contains("jabong"))
                brand_image.setImageResource(R.drawable.jabong_icon);
            else if(brand_name.contains("amazon"))
                brand_image.setImageResource(R.drawable.amazon_icon);
            else if(brand_name.contains("flipkart"))
                brand_image.setImageResource(R.drawable.flipkart_icon);
        }

		// Feed image
		if (item.getImage() != null) {
            Picasso.with(activity).load(item.getImage()).into(feedImageView);
			feedImageView.setVisibility(View.VISIBLE);
		} else {
			feedImageView.setVisibility(View.GONE);
		}

		return convertView;
	}

}
