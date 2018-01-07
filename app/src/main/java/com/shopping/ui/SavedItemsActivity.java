package com.shopping.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.shopping.url.parse.ProductData;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import info.shopping.listviewfeed.R;
import com.shopping.adapter.ShortlistedProductsAdapter;
import com.shopping.swipedismiss.SwipeDismissListViewTouchListener;

public class SavedItemsActivity extends FragmentActivity {
	private static final String TAG = SavedItemsActivity.class.getSimpleName();
	private ListView listView;
	private ShortlistedProductsAdapter listAdapter;
	private List<ProductData> feedItems;
	private String URL_FEED = "http://api.androidhive.info/feed/feed.json";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feed);

		listView = (ListView) findViewById(R.id.list);

		feedItems = new ArrayList<ProductData>();

		listAdapter = new ShortlistedProductsAdapter(this, feedItems);
		listView.setAdapter(listAdapter);
		
		// These two lines not needed,
		// just to get the look of facebook (changing background color & hiding the icon)
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //loadFeedDataFromJason();
        loadFeedDataFromSavedItems();
        addSwipeToDismiss();
	}

    private void addSwipeToDismiss() {
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissLeft(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    feedItems.remove(position);
                                }
                                listAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissRight(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    goShopping(feedItems.get(position).getUrl());
                                }
                                listAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onProductClick(ListView listView, int pos) {
                                ProductData productData = feedItems.get(pos);
                                ProductInfoFragment dialog = new ProductInfoFragment();
                                dialog.setProductData(productData);
                                dialog.show(getSupportFragmentManager() , "QuickContactFragment");
                                Log.d(TAG, "Product Clicked");
                            }
                        });
        listView.setOnTouchListener(touchListener);
    }

    private void loadFeedDataFromSavedItems() {
        Vector<ProductData> savedProducts = ProductData.ourGlobalSavedProducts;

        for (int i = 0; i < savedProducts.size(); i++) {
            ProductData feedObj = savedProducts.get(i);
            feedItems.add(feedObj);
        }
        // notify data changes to list adapater
        listAdapter.notifyDataSetChanged();
    }

    /*
    private void loadFeedDataFromJason() {
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }*/

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	/*private void parseJsonFeed(JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("feed");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				FeedItem item = new FeedItem();
				item.setId(feedObj.getInt("id"));
				item.setName(feedObj.getString("name"));

				// Image might be null sometimes
				String image = feedObj.isNull("image") ? null : feedObj.getString("image");
				item.setImge(image);
				item.setStatus(feedObj.getString("status"));
				item.setProfilePic(feedObj.getString("profilePic"));
				item.setTimeStamp(feedObj.getString("timeStamp"));

				// url might be null sometimes
				String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
				item.setUrl(feedUrl);

				feedItems.add(item);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}*/

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/

    public void onSavedProductClick(View view) {
        String product_url = (String)view.getTag();
        goShopping(product_url);
    }

    private void goShopping(String product_url) {
        Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
        myWebLink.setData(Uri.parse(product_url));
        startActivity(myWebLink);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition  (R.anim.newsfadein, R.anim.right_slide_out);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition  (R.anim.newsfadein, R.anim.right_slide_out);
                break;
        }
        return true;
    }

}
