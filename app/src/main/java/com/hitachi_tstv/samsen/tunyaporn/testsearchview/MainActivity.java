package com.hitachi_tstv.samsen.tunyaporn.testsearchview;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    //Explicit
    private SearchView mSearchView;
    private ListView mListView;
    private android.widget.Filter filter;
    private static final String myUrl = "http://service.eternity.co.th/tires/system/centerservice/getVehicle.php";

//    private final String[] mStrings = Cheeses.sCheesesSTRINGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        setContentView(R.layout.activity_main);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);

        SyncVehicle syncVehicle = new SyncVehicle(this, myUrl, mListView);
        syncVehicle.execute();


//        Log.d("TV2", "Cheese ==> " + mStrings);

    }

    private class SyncVehicle extends AsyncTask<Void, Void, String> {
        //Explicit
        private Context context;
        private String myURLString;
        private ListView listView;
        private String[] licenseStrings, idStrings;

        public SyncVehicle(Context context, String myURLString, ListView listView) {
            this.context = context;
            this.myURLString = myURLString;
            this.listView = listView;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(myURLString).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                Log.d("LOG1", "e DoInBack ==> " + e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("LOG1", "JSON ==> " + s);

            try {
                JSONArray jsonArray = new JSONArray(s);

                licenseStrings = new String[jsonArray.length()];
                idStrings = new String[jsonArray.length()];

                for (int i = 0;i < jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    licenseStrings[i] = jsonObject.getString("veh_license");
                    idStrings[i] = jsonObject.getString("veh_id");
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, licenseStrings);
                filter = arrayAdapter.getFilter();
                listView.setAdapter(arrayAdapter);




                setupSearchView();


            } catch (Exception e) {
                Log.d("LOG1", "e OnPost ==> " + e);
            }
        }
    }


    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            filter.filter(newText);
        }
        Log.d("TV1", "newText ==> " + newText.toString());
        return true;
    }
}
