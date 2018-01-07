package com.shopping.ui;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.shopping.url.parse.ProductData;

import java.util.HashMap;

import info.shopping.listviewfeed.R;

public class ProductInfoFragment extends DialogFragment implements BaseSliderView.OnSliderClickListener {
    private static String TAG = "ProductInfoFragment";

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

    private ImageView image;
	private ProductInfoPagerAdapter adapter;
    private ProductData myProductData;
    private SliderLayout mDemoSlider;


    public static ProductInfoFragment newInstance() {
		ProductInfoFragment f = new ProductInfoFragment();
		return f;
	}

    public ProductInfoFragment() {
        myProductData = null;
    }

    public void setProductData(ProductData productData) {

        myProductData = productData;
        Log.d(TAG, " Title " + myProductData.getTitle() + " Details " + myProductData.getDetails());
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (getDialog() != null) {
            Log.d("INFO: Setting Gravity ", " ");
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setGravity(Gravity.CENTER_VERTICAL);
			getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		}


		View root = inflater.inflate(R.layout.product_info, container, false);
        mDemoSlider = (SliderLayout)root.findViewById(R.id.slider);
        handleImageSlider();

        tabs = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
		pager = (ViewPager) root.findViewById(R.id.pager);

        image = (ImageView) root.findViewById(R.id.image);
        if(null != myProductData) {
            //Picasso.with(getActivity()).load(myProductData.getImage()).fit().centerInside().into(image);
            TextView title = (TextView) root.findViewById(R.id.image_footer_title);
            //title.setText(myProductData.getTitle());
        }

        adapter = new ProductInfoPagerAdapter();
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);

		return root;
	}

    private void handleImageSlider() {
        int img_id = 0;
        HashMap<String,String> url_maps = new HashMap<String, String>();
        for (int i = 0; i < 4; i++ ) {
            String image_url = myProductData.getImage(i);

            if(!image_url.equals("NULL")) {
                Log.d("PDT Pager add ", "imgcurl " + i + " : " + image_url);
                img_id++;
                url_maps.put(myProductData.getTitle() + img_id, image_url);
            }
        }


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        /*ListView l = (ListView)findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(ProductShortListActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

	@SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();

		// change dialog width
		if (getDialog() != null) {

			int fullWidth = getDialog().getWindow().getAttributes().width;

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				fullWidth = size.x;
			} else {
				Display display = getActivity().getWindowManager().getDefaultDisplay();
				fullWidth = display.getWidth();
			}

			final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
					.getDisplayMetrics());

			int w = fullWidth - padding;
			int h = getDialog().getWindow().getAttributes().height;

			getDialog().getWindow().setLayout(w, h);
		}
	}

	public class ProductInfoPagerAdapter extends PagerAdapter /*implements PagerSlidingTabStrip.IconTabProvider*/ {

		private final int[] ICONS = { R.drawable.ic_launcher_gplus, R.drawable.ic_launcher_gmail,
				R.drawable.ic_launcher_gmaps, R.drawable.ic_launcher_chrome };

        private final String[] TITLES = { "Price", "Discount", "Description", "Rating" , "Website"};


        public ProductInfoPagerAdapter() {
			super();
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        /*
        @Override
		public int getPageIconResId(int position) {	return ICONS[position]; }
		*/

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// looks a little bit messy here
			TextView v = new TextView(getActivity());
			v.setBackgroundResource(R.color.background_window);

            switch(position) {
                case 0:
                    v.setText("Rs " + myProductData.getPrice());
                    break;
                case 1:
                    v.setText(myProductData.getDiscount() + "%");
                    break;
                case 2:
                    v.setText(myProductData.getDetails());
                    break;
                case 3:
                    v.setText(myProductData.getRating() + "/5");
                    break;
                case 4:
                    v.setText(myProductData.getBrand());
            }
			//v.setText(myProductData.getDetails());//"PAGE " + (position + 1));



			final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
			v.setPadding(padding, padding, padding, padding);
			v.setGravity(Gravity.CENTER);
			container.addView(v, 0);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object view) {
			container.removeView((View) view);
		}

		@Override
		public boolean isViewFromObject(View v, Object o) {
			return v == ((View) o);
		}

	}

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

}
