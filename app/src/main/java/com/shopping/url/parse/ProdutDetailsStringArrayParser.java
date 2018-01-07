package com.shopping.url.parse;

import android.content.Context;
import android.util.Log;

import com.shopping.adapter.StackedProductsAdapter;

public class ProdutDetailsStringArrayParser {
	private static final String TAG = "ProdutDetailsStringArrayParser";
	Context myContext = null;
    StackedProductsAdapter myAdapter = null;

    public ProdutDetailsStringArrayParser(Context context, StackedProductsAdapter adapter) {
        myContext = context;
        myAdapter = adapter;
    }

    public void parse(ProductDetailsStringArrays.Categories cat, int productIndex) {
        String [][] pdtArray = ProductDetailsStringArrays.getCategoryDataArray(cat);
        if(productIndex >= pdtArray.length)
            return;;

		Log.d(TAG, "Trying to parse " + productIndex);
        String [] pd = pdtArray[productIndex];
        ProductData productData =  new ProductData(pd[ProductDetailsStringArrays.Columns.Product_Name.ordinal()],
                pd[ProductDetailsStringArrays.Columns.Website.ordinal()],
                pd[ProductDetailsStringArrays.Columns.Description.ordinal()],
                pd[ProductDetailsStringArrays.Columns.Price.ordinal()],
                pd[ProductDetailsStringArrays.Columns.Image_url_1.ordinal()],
                pd[ProductDetailsStringArrays.Columns.Product_Url.ordinal()],
                pd[ProductDetailsStringArrays.Columns.Discount.ordinal()],
                pd[ProductDetailsStringArrays.Columns.Ratings.ordinal()]
                );
        productData.setImage(1, pd[ProductDetailsStringArrays.Columns.Image_url_2.ordinal()]);
        productData.setImage(2, pd[ProductDetailsStringArrays.Columns.Image_url_3.ordinal()]);
        productData.setImage(3, pd[ProductDetailsStringArrays.Columns.Image_url_4.ordinal()]);

        myAdapter.addProductView(productData);
    }

}
