package tatteam.com.app_common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import tatteam.com.app_common.R;
import tatteam.com.app_common.entity.MyAppEntity;
import tatteam.com.app_common.entity.MyExtraAppsEntity;
import tatteam.com.app_common.util.AppConstant;
import tatteam.com.app_common.util.AppLocalSharedPreferences;
import tatteam.com.app_common.util.AppParseUtil;
import tatteam.com.app_common.util.CommonUtil;

/**
 * Created by ThanhNH on 10/11/2015.
 */
public class MoreAppsDialog extends Dialog implements AppConstant, View.OnClickListener {

    private static final int TAB_MODE_APPS = 1;
    private static final int TAB_MODE_GAMES = 2;

    private TextView txtApps;
    private TextView txtGames;
    private RecyclerView recyclerView;
    private TextView txtClose;
    private TextView txtVisitStore;
    private TextView txtLoading;
    private View layoutLoading;
    private ProgressBar progressBar;

    private RecyclerView.Adapter adapter;
    private Future getExtraAppFuture;
    private MyExtraAppsEntity myExtraApps;
    private int tabMode = TAB_MODE_APPS;
    private String url;


    public MoreAppsDialog(Context context, String url) {
        super(context);
        this.url = url;
        if (this.url == null || this.url.isEmpty()) {
            this.url = DEFAULT_MORE_APP_URL;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_more_apps);
        this.setCanceledOnTouchOutside(false);
        tabMode = TAB_MODE_APPS;
        findViews();
        setupRecycleView();
        downloadData();
        displayData();
    }

    private void downloadData() {
        //load from local first
        String localJson = AppLocalSharedPreferences.getInstance().getMyExtraAppsString();
        if (localJson != null && !localJson.isEmpty()) {
            layoutLoading.setVisibility(View.GONE);
            myExtraApps = AppParseUtil.parseExtraApps(localJson);
            if (!myExtraApps.my_apps.isEmpty()) {
                displayData();
            } else {
                AppLocalSharedPreferences.getInstance().setMyExtraApps("");
            }
        }

        //load from server
        getExtraAppFuture = Ion.with(getContext())
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result != null) {
                            String localJson = AppLocalSharedPreferences.getInstance().getMyExtraAppsString();
                            if (localJson == null || localJson.isEmpty()) {
                                myExtraApps = AppParseUtil.parseExtraApps(result);
                                displayData();
                            }
                            AppLocalSharedPreferences.getInstance().setMyExtraApps(result);
                            layoutLoading.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void displayData() {
        if (tabMode == TAB_MODE_APPS) {
            txtApps.setBackgroundResource(R.color.tab_selected);
            txtGames.setBackgroundResource(R.color.tab_normal);
        } else if (tabMode == TAB_MODE_GAMES) {
            txtGames.setBackgroundResource(R.color.tab_selected);
            txtApps.setBackgroundResource(R.color.tab_normal);
        }
        if (myExtraApps != null) {
            if (tabMode == TAB_MODE_APPS) {
                adapter = new MyAdapter(getContext(), myExtraApps.my_apps);
            } else if (tabMode == TAB_MODE_GAMES) {
                adapter = new MyAdapter(getContext(), myExtraApps.my_games);
            }
            recyclerView.setAdapter(adapter);
        }
    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutLoading = findViewById(R.id.layout_loading);
        txtLoading = (TextView) findViewById(R.id.txt_loading);
        txtApps = (TextView) findViewById(R.id.txt_apps);
        txtGames = (TextView) findViewById(R.id.txt_games);
        txtClose = (TextView) findViewById(R.id.txt_close);
        txtVisitStore = (TextView) findViewById(R.id.txt_visit_store);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        txtApps.setOnClickListener(this);
        txtGames.setOnClickListener(this);
        txtClose.setOnClickListener(this);
        txtVisitStore.setOnClickListener(this);
    }

    private void setupRecycleView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onClick(View v) {
        if (v == txtApps) {
            if (tabMode != TAB_MODE_APPS) {
                tabMode = TAB_MODE_APPS;
                displayData();
            }
        } else if (v == txtGames) {
            if (tabMode != TAB_MODE_GAMES) {
                tabMode = TAB_MODE_GAMES;
                displayData();
            }
        } else if (v == txtClose) {
            dismiss();
        } else if (v == txtVisitStore) {
            if (myExtraApps != null) {
                CommonUtil.openDeveloperPageOnGooglePlay(getContext(), myExtraApps.my_pub_name);
                dismiss();
            }
        }
    }

    @Override
    public void dismiss() {
        if (getExtraAppFuture != null) {
            getExtraAppFuture.cancel(true);
        }
        super.dismiss();
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<MyAppEntity> myApps;
        private Context activity;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imgAppIcon;
            public TextView txtAppName;
            public TextView txtAppDescription;

            public ViewHolder(View item) {
                super(item);
                imgAppIcon = (ImageView) item.findViewById(R.id.img_app_icon);
                item.findViewById(R.id.layout_container).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyAppEntity myAppEntity = myApps.get(getAdapterPosition());
                        CommonUtil.openApplicationOnGooglePlay(activity, myAppEntity.package_name);
                    }
                });

                txtAppName = (TextView) item.findViewById(R.id.txt_app_name);
                txtAppDescription = (TextView) item.findViewById(R.id.txt_app_description);
            }
        }

        public MyAdapter(Context activity, List<MyAppEntity> myApps) {
            this.activity = activity;
            this.myApps = myApps;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_apps, parent, false);
            ViewHolder vh = new ViewHolder(item);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.txtAppName.setText("");
            holder.txtAppDescription.setText("");
            MyAppEntity myApp = myApps.get(position);
            Ion.with(holder.imgAppIcon)
                    .placeholder(R.drawable.more_app_default_icon)
                    .error(R.drawable.more_app_default_icon)
                    .load(myApp.image);
            holder.txtAppName.setSelected(true);
            holder.txtAppName.setText(myApp.name);
            holder.txtAppDescription.setText(myApp.description);

        }

        @Override
        public int getItemCount() {
            return myApps.size();
        }
    }
}
