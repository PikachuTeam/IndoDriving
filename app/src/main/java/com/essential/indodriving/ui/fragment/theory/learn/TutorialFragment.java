package com.essential.indodriving.ui.fragment.theory.learn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.essential.indodriving.R;
import com.essential.indodriving.data.driving.DrivingDataSource;
import com.essential.indodriving.ui.base.Constants;
import com.essential.indodriving.ui.base.MyBaseFragment;

/**
 * Created by dongc_000 on 2/20/2016.
 */
public class TutorialFragment extends MyBaseFragment {

    private int type;

    private TextView textViewInternetError;
    private WebView tutorialContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_tutorial);
    }

    @Override
    protected boolean enableButtonBack() {
        return false;
    }

    @Override
    protected boolean enableButtonClear() {
        return true;
    }

    @Override
    protected int getLayoutResIdContentView() {
        return R.layout.fragment_tutorial;
    }

    @Override
    protected void onCreateContentView(View rootView, Bundle savedInstanceState) {
        findViews(rootView);
        if (isNetworkAvailable()) {
            tutorialContainer.setVisibility(View.VISIBLE);
            textViewInternetError.setVisibility(View.GONE);
            tutorialContainer.setWebViewClient(new MyBrowser());
            tutorialContainer.getSettings().setJavaScriptEnabled(true);
            tutorialContainer.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            switch (type) {
                case DrivingDataSource.TYPE_SIM_A:
                    tutorialContainer.loadUrl("https://www.youtube.com/results?search_query=Ujian+Praktek+SIM+A");
                    break;
                case DrivingDataSource.TYPE_SIM_C:
                    tutorialContainer.loadUrl("https://www.youtube.com/results?search_query=Ujian+Praktek+SIM+C");
                    break;
            }
        } else {
            tutorialContainer.setVisibility(View.GONE);
            textViewInternetError.setVisibility(View.VISIBLE);
        }
    }

    private void findViews(View rootView) {
        textViewInternetError = (TextView) rootView.findViewById(R.id.textViewInternetError);
        tutorialContainer = (WebView) rootView.findViewById(R.id.tutorialContainer);
    }

    private void getData() {
        Bundle bundle = getArguments();
        type = bundle.getInt(Constants.BUNDLE_TYPE, DrivingDataSource.TYPE_SIM_A);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity()
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public void onBackPressed() {
        if (tutorialContainer.canGoBack()) {
            tutorialContainer.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tutorialContainer.destroy();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
