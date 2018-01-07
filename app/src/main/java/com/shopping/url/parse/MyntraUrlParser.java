package com.shopping.url.parse;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.shopping.adapter.StackedProductsAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class MyntraUrlParser {
	private static final String TAG = "MyntraUrlParser";
	Context myContext = null;
    StackedProductsAdapter myAdapter = null;

    public MyntraUrlParser(Context context, StackedProductsAdapter adapter) {
        myContext = context;
        myAdapter = adapter;
    }

    public void parse(String url) {
		Log.d(TAG, "Trying to parse " + url);
		new HttpAsyncTask().execute(url);		
	}
	
	public class HttpAsyncTask extends AsyncTask<String, Void, ProductData> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            //txtFooterTitle.setText("Loading...");
            super.onPreExecute();
        }

        @Override
        protected ProductData doInBackground(String... urls) {
            return getProductDataFromUrl(urls[0]);
        }

        @Override
        protected void onPostExecute(ProductData productData) {
            if(productData != null) // Can be null when timeout
                myAdapter.addProductView(productData);
        }

        protected ProductData getProductDataFromUrl(String url) {
            ProductData productData = null;
            Document doc = null;
            try {
                Log.d(TAG, "doInBackground ..........");
                doc = Jsoup.connect(url).userAgent("Mozilla").get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            ConnectivityManager connectivityService = (ConnectivityManager)myContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (doc != null && connectivityService != null && (connectivityService.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) ||((doc != null && connectivityService.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED))) {

                productData = extractTitleAndImageFromDoc(url, doc);
                //String imageUrl = extractYhumbnailFromDoc(doc);
               // ((SavedItemsActivity)myContext).addProductView(imageUrl, 0);
                //myAdapter.notifyDataSetChanged();
            }else if (connectivityService.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connectivityService.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED ) {
                //Not connected.
                //txtFooterTitle.setText("Connect to Internet...");
            }else{
                //txtFooterTitle.setText("Connection Problem...");
            }

            return productData;
        }

        protected ProductData extractTitleAndImageFromDoc(String url, Document doc) {
            Log.d("URL PARSER extractTitleAndImageFromDoc for url", url);

            //Elements x = doc.select("body > div.container.details > div.summary > div.images.active > div.blowup > div");
            Element summary = doc.select("body > div.container.details > div.summary").first();
            String title = summary.getElementsByClass("title").text();
            Log.d("URL PARSER title", title);

            Element blowup_image1 = summary.getElementsByClass("blowup").first();
            String imageurl = blowup_image1.child(1).attr("src");
            Log.d("URL PARSER img url", imageurl);

            Element info = doc.select("body > div.container.details > div.more-info").first();
            String details = info.child(1).child(1).text();
            Log.d("URL PARSER Details: ", details);

            String price = summary.child(1).child(2).ownText();
            Log.d("URL PARSER Price: ", price);


            return new ProductData(title, "Myntra", details, price, imageurl, url, "", "");
        }

        protected String extractBlowupImageFromDoc(Document doc) {
            //Elements x = doc.select("body > div.container.details > div.summary > div.images.active > div.blowup > div");
            Element images = doc.select("body > div.container.details > div.summary > div.images").first();
            Element blowup = images.child(1);
            Element blowup_image = blowup.child(1);
            Log.d("URL PARSER blowup_image", blowup_image.toString());

            String url = blowup_image.attr("src");
            Log.d("URL PARSER blowup_image src = ", url);
            return url;
        }
        
        protected String extractYhumbnailFromDoc(Document doc) {
            //You are connected, do something online.
        	//Log.d("URL PARSER", " Doc " + doc.toString());

        	//Elements x = doc.select("body > div.container.details > div.summary > div.images.active > div.blowup > div");
        	Element thumb_block = doc.select("body > div.container.details > div.summary > div.images > div.thumbs-container > div.thumbs-scroll").first();
        	Element thumbs = thumb_block.child(1);
        	Element first_thumb = thumbs.child(0);
    		Log.d("URL PARSER first_thumb", first_thumb.toString());

        	String url = first_thumb.attr("src");
    		Log.d("URL PARSER first_thumb src = ", url);
            return url;
        }
    }
}
