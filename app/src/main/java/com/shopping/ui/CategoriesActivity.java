package com.shopping.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.shopping.url.parse.ProductDetailsStringArrays;
import com.squareup.picasso.Picasso;

import info.shopping.listviewfeed.R;

public class CategoriesActivity extends Activity {

    int [] imageResId = { R.id.c1, R.id.c2, R.id.c3, R.id.c4, R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9, R.id.c10, R.id.c11, R.id.c12, R.id.c13, R.id.c14, R.id.c15, R.id.c16 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));

        int imgId = ProductDetailsStringArrays.Columns.Image_url_1.ordinal();

        for(ProductDetailsStringArrays.Categories c :ProductDetailsStringArrays.Categories.values()) {
            int index = c.ordinal();
            if(index < imageResId.length) {
                ImageButton b = (ImageButton) findViewById(imageResId[index]);
                String url = ProductDetailsStringArrays.getCategoryDataArray(c)[0][imgId];
                Picasso.with(this).load(url).fit().centerInside().into(b);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnPdtCategoryClick(View v) {
        if(v.getTag() != null) {
            Intent i = new Intent(this, ProductShortListActivity.class);
            i.putExtra("CATEGORY", v.getTag().toString());
            Log.d("PdtSelecton", "Selected cat = " + v.getTag().toString());
            startActivity(i);
        }

    }
}
