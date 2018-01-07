package com.shopping.url.parse;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class UrlParser {
	private static final String TAG = "UrlParser";
	Context myContext = null;
	
	public UrlParser(Context context) {
		myContext = context;		
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
            //((SavedItemsActivity)myContext).addProductView(productData);

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

            return new ProductData(title, "Myntra", "", "", imageurl, url, "", "");
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

        	/*for(int i = 0; i < 2; i ++) {
        		Log.d("URL PARSER thumb" + i , " Select x("+ i + ") = " + thumb.child(i).toString());
        	}
        	Element x = doc.select("body > div.container.details > div.summary > div.images").first();
        	//x.attr(arg0)

        	for(int i = 0; i < 3; i ++) {
        		Log.d("URL PARSER" + i , " Select x("+ i + ") = " + x.child(i).toString());
        	}        	*/
    		
            /*Elements title_url = doc.select("[itemprop=itemListElement]");
            Elements image = doc.select("[height=221]");
            for(int i=0; i<title_url.size()-4; i++){
                String productTitle = title_url.get(i).text();
                String productUrl = title_url.get(i).attr("abs:href");
                String productImage = image.get(i).attr("abs:src");
                ProductData data = new ProductData(productTitle, productImage, productUrl);
            }        	*/
        }

    }


}
