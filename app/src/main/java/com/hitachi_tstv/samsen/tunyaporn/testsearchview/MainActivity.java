package com.hitachi_tstv.samsen.tunyaporn.testsearchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    //Explicit
    private SearchView mSearchView;
    private ListView mListView;
    private android.widget.Filter filter;

    private final String[] mStrings = Cheeses.sCheesesSTRINGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        setContentView(R.layout.activity_main);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, mStrings));
        mListView.setTextFilterEnabled(true);

        Log.d("TV2", "Cheese ==> " + mStrings);
        setupSearchView();
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
            mListView.setFilterText(newText.toString());
        }
        Log.d("TV1", "newText ==> " + newText.toString());
        return true;
    }
}
