package com.shopping.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.shopping.url.parse.ProductData;
import com.shopping.url.parse.ProductDetailsStringArrays;

import java.util.Vector;

import info.shopping.listviewfeed.R;
import com.shopping.adapter.StackedProductsAdapter;


public class ProductShortListActivity extends FragmentActivity {
	private static final String TAG = "ProductShortListActivity";
    private Vector<ProductData> myProductList;
    private StackedProductsAdapter myAdapter;

	RelativeLayout parentView;
    RelativeLayout myButton_layout;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shortlist);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));

		parentView = (RelativeLayout) findViewById(R.id.layoutview);
        myProductList = new Vector<ProductData>();
        myAdapter = new StackedProductsAdapter(this, myProductList, parentView);


        //
        String cat = getIntent().getStringExtra("CATEGORY");
        Log.d(TAG, "Got cat = " + cat);

        if(cat != null) {
            for (ProductDetailsStringArrays.Categories c : ProductDetailsStringArrays.Categories.values())  {
                if(c == ProductDetailsStringArrays.Categories.NULL)
                    continue;;

                if(ProductDetailsStringArrays.CategoriesNames[c.ordinal()].equals(cat)) {
                    Log.d(TAG, "Setting cat = " + cat + " c = " + c.toString());

                    myAdapter.setCategory(c);
                    break;
                }
            }
        }

        myButton_layout = (RelativeLayout)findViewById(R.id.button_layout);
    }
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
        final ViewTreeObserver observer= myButton_layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                myAdapter.setCardHeight(myButton_layout.getHeight()+ 2*myButton_layout.getPaddingBottom());
                myAdapter.addNextProducts(10);
                myButton_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

	}

    public void onLikeButtonPressed(View view) {
        Log.d(TAG, "Liked it. ");
        myAdapter.likeProductView(null);
    }

    public void onInfoButtonPressed(View view) {
        Log.d(TAG, "Well Need more info. ");
        ProductData productData = myAdapter.getProductForTopViewFromStack();
        ProductInfoFragment dialog = new ProductInfoFragment();
        dialog.setProductData(productData);
        dialog.show(getSupportFragmentManager() , "QuickContactFragment");
    }

    public void onPassButtonPressed(View view) {
        Log.d(TAG, "Pass it. ");
        myAdapter.passProductView(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SavedItemsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in, R.anim.newsfadeout);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Callback from StackedProductsAdapter
    public void saveProduct(ProductData productData) {
        ProductData.ourGlobalSavedProducts.add(productData);
        Log.d(TAG, "Saved Product: " + productData.getTitle());
    }

    /*protected void addProductView(int imageResource, int index) {
        ImageView img = (ImageView)relView.findViewById(R.id.product_imageView);
        img.setBackgroundResource(imageResource);
    }*/
}