package com.shopping.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopping.ui.ProductShortListActivity;
import com.shopping.url.parse.MyntraUrlParser;
import com.shopping.url.parse.MyntraUrls;
import com.shopping.url.parse.ProductData;
import com.shopping.url.parse.ProductDetailsStringArrays;
import com.shopping.url.parse.ProdutDetailsStringArrayParser;
import com.squareup.picasso.Picasso;

import java.util.Vector;

import info.shopping.listviewfeed.R;

public class StackedProductsAdapter {
    private static final String TAG = "StackedProductsAdapter";

    private MyntraUrlParser myntraParser;
    private ProdutDetailsStringArrayParser produtDetailsStringArrayParser;
    ProductDetailsStringArrays.Categories myCat = ProductDetailsStringArrays.Categories.NULL;

    private Activity activity;
	private LayoutInflater inflater;
	private Vector<ProductData> productDataList;
    private int currentUrlIndex = 0;
    private RelativeLayout myProductsStackView;

    int window_width;
    int window_height;

    int screenCenter;

    int left_right_margin = 80;
    int top_margin = 40;
    int card_width = 0;// = window_width - 2* left_right_margin;
    int card_height = 700;

	public StackedProductsAdapter(Activity activity, Vector<ProductData> productDataList, RelativeLayout stackView) {
		this.activity = activity;
		this.productDataList = productDataList;
        myntraParser = new MyntraUrlParser(activity, this);
        produtDetailsStringArrayParser = new ProdutDetailsStringArrayParser(activity, this);
        currentUrlIndex = 0;
        myProductsStackView = stackView;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        window_width = size.x;
        window_height = size.y;
        screenCenter = window_width / 2;
        card_width = window_width - 2* left_right_margin;
    }

    public void setCategory(ProductDetailsStringArrays.Categories c) { myCat = c; }

    public void setCardHeight(int button_layout_height) {
        Log.d(TAG, "button_layout_height = " + button_layout_height);
        card_height = window_height - button_layout_height - 2*top_margin - activity.getActionBar().getHeight();
    }

	public int getCount() {
		return productDataList.size();
	}

	public Object getItem(int location) {
		return productDataList.get(location);
	}

	public long getItemId(int position) {
		return position;
	}

	public RelativeLayout getView(int position, View convertView, ViewGroup parent) {
        ProductData productData = productDataList.get(position);

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.product_card, null);


        RelativeLayout productView = new RelativeLayout(activity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(card_width, card_height);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        productView.setLayoutParams(layoutParams);

        ImageView img = (ImageView)convertView.findViewById(R.id.product_imageView);
        Picasso.with(activity).load(productData.getImage()).fit().centerInside().into(img);
        TextView title = (TextView)convertView.findViewById(R.id.pdt_title);
        title.setText(productData.getTitle());

        productView.addView(convertView);
        //productView.setX(left_right_margin);
        productView.setY(top_margin - getYShiftForIndex(position));
        productView.setTag(position);
        //productView.setRotation(getRotationAngleForIndex(position));
        return productView;
    }


    static int pdt_index = 1;
    private float getYShiftForIndex(int index) {
        pdt_index++;
        final int [] rotationAngleArray = { -8, -4, 0, +4, 8 };
        float shift = rotationAngleArray[pdt_index % 5];
        Log.d(TAG, "Y Shift = " + shift + " for index = " + pdt_index);
        return shift;
    }


    public void addNextProducts(int numberOf) {
        if(myCat == ProductDetailsStringArrays.Categories.NULL) {
            int maxIndex = MyntraUrls.all_urls.length;
            for (int index = 0; index < numberOf && index < maxIndex; index++) {
                String url = MyntraUrls.all_urls[currentUrlIndex];
                currentUrlIndex++;
                myntraParser.parse(url);
            }
        }
        else {
            int maxIndex = ProductDetailsStringArrays.getCategoryDataArray(myCat).length;
            for (int index = 0; index < numberOf && index < maxIndex; index++) {
                produtDetailsStringArrayParser.parse(myCat, currentUrlIndex);
                currentUrlIndex++;
            }
        }
    }

    public void addProductView(ProductData productData) {
        productDataList.add(productData);
        RelativeLayout productView = getView(productDataList.size() -1, null, null);
        setOnTouchListener(productView);
        myProductsStackView.addView(productView, 0);
    }

    public void likeProductView(RelativeLayout productView) {
        if(productDataList.isEmpty())
            return;
        if(productView == null)
            productView = getTopViewFromStack();
        ProductData productData = getProductForTopViewFromStack();
        ((ProductShortListActivity)activity).saveProduct(productData);
        removeProductView(productView);
    }

    public void passProductView(RelativeLayout productView) {
        if(productDataList.isEmpty())
            return;
        if(productView == null)
            productView = getTopViewFromStack();
        removeProductView(productView);
    }

    public void removeProductView(RelativeLayout productView) {
        TextView title = (TextView)productView.findViewById(R.id.pdt_title);
        Log.d(TAG, "Removing pdt title: " + productDataList.get(0).getTitle() + " view title: " + title.getText().toString());
        myProductsStackView.removeView(productView);
        productDataList.remove(0);
        addNextProducts(1);
    }

    // Layout stack is 0 at bottom most view and last view at is top/visible.
    // productList's first item becomes last children of view Stack so that its visible on top.
    private RelativeLayout getTopViewFromStack() {
        int viewIndex = productDataList.size() -1;
        return (viewIndex >= 0) ? (RelativeLayout)myProductsStackView.getChildAt(viewIndex) : null;
    }

    public ProductData getProductForTopViewFromStack() {
        return productDataList.get(0);
    }

    public void removeTopProductView() {
        RelativeLayout productView = getTopViewFromStack();
        if(productView != null)
            removeProductView(productView);
    }


    private float getRotationAngleForIndex(int index) {
        final int [] rotationAngleArray = { 0, -2, +2, -1, 1, 3};
        return rotationAngleArray[index % 5];
    }

    private Resources getResources() {
        return activity.getResources();
    }

    int x_cord, y_cord;
    int Likes = 0;
    float alphaValue = 0;

    protected void  setOnTouchListener(final RelativeLayout product_view) {
        final ImageView imageLike = (ImageView)product_view.findViewById(R.id.stamp_like);
        final ImageView imagePass = (ImageView)product_view.findViewById(R.id.stamp_pass);
        imageLike.setVisibility(View.INVISIBLE);
        imagePass.setVisibility(View.INVISIBLE);


        View.OnTouchListener touchListener = new View.OnTouchListener() {
            int touchDownX = 0;
            int touchDownY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x_cord = (int) event.getRawX();
                y_cord = (int) event.getRawY();

                //product_view.setX(x_cord - screenCenter + 40);
                //product_view.setY(y_cord - 150);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownX = (int) event.getRawX();
                        touchDownY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        x_cord = (int) event.getRawX();
                        y_cord = (int) event.getRawY();
                        int deltaX = x_cord - touchDownX;
                        int deltaY = y_cord - touchDownY;

                        product_view.setX(left_right_margin + deltaX);
                        product_view.setY(top_margin + deltaY);

                        product_view.setRotation((float) ((deltaX) * (Math.PI / 32)));

                        if (deltaX > 0) {
                            if (deltaX > (window_width / 4)) {
                                imageLike.setVisibility(View.VISIBLE);
                                if (deltaX > (3/8)*window_width ) {
                                    Likes = 2;
                                } else {
                                    Likes = 0;
                                }
                            } else {
                                Likes = 0;
                                imageLike.setVisibility(View.INVISIBLE);
                            }
                            imagePass.setVisibility(View.INVISIBLE);
                        } else {
                            if (deltaX < - (window_width / 4)) {
                                imagePass.setVisibility(View.VISIBLE);
                                if (deltaX < - (3/8)*window_width) {
                                    Likes = 1;
                                } else {
                                    Likes = 0;
                                }
                            } else {
                                Likes = 0;
                                imagePass.setVisibility(View.INVISIBLE);
                            }
                            imageLike.setVisibility(View.INVISIBLE);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        x_cord = (int) event.getRawX();
                        y_cord = (int) event.getRawY();
                        deltaX = x_cord - touchDownX;
                        deltaY = y_cord - touchDownY;
                        if(deltaX == 0 && deltaY ==0)
                            ((ProductShortListActivity)activity).onInfoButtonPressed(null);

                        Log.e("X Point", "" + x_cord + " , Y " + y_cord);
                        imagePass.setVisibility(View.INVISIBLE);
                        imageLike.setVisibility(View.INVISIBLE);

                        if (Likes == 0) {
                            Log.e("Event Status", "Nothing");
                            product_view.setX(40);
                            product_view.setY(40);
                            product_view.setRotation(0);
                        } else if (Likes == 1) {
                            Log.e("Event Status", "Passed");
                            passProductView(product_view);
                        } else if (Likes == 2) {
                            Log.e("Event Status", "Liked");
                            likeProductView(product_view);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
        product_view.setOnTouchListener(touchListener);
    }
}
