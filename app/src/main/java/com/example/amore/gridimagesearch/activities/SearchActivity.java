package com.example.amore.gridimagesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.example.amore.gridimagesearch.adapters.EndlessScrollListener;
import com.example.amore.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.amore.gridimagesearch.models.Filter;
import com.example.amore.gridimagesearch.models.ImageResult;
import com.example.amore.gridimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;
    private final int numRes = 8;

    private StaggeredGridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    private String searchUrl;
    private int index = 0;

    public Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();

        imageResults = new ArrayList<>();
        aImageResults = new ImageResultsAdapter(this, imageResults);

        gvResults.setAdapter(aImageResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                index += numRes;
                customLoadMoreDataFromApi();
                return true;
            }
        });
    }

    private void setupViews() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#34495e")));
        gvResults = (StaggeredGridView) findViewById(R.id.gvResults);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult imageResult = imageResults.get(position);

                intent.putExtra("imageResult", imageResult);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                onImageSearch(searchView.getQuery().toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onImageSearch(String query) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Unable to connect to the Internet. " +
                    "\nPlease try again later.", Toast.LENGTH_LONG).show();
        }

        index = 0;
        imageResults.clear();

        searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q=" + query + "&v=1.0&rsz=" + numRes;

        if (filter != null) {
            searchUrl = applyFilters(searchUrl);
        }

        customLoadMoreDataFromApi();
    }

    private void customLoadMoreDataFromApi() {
        String searchUrlWithIndex = searchUrl + "&start=" + index;

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(searchUrlWithIndex, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray imageResultsJSON;
                try {
                    imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String applyFilters(String baseUrl) {
        String augUrl = baseUrl;
        if (filter.filterSize != null) {
            augUrl += "&imgsz=" + filter.filterSize;
        }

        if (filter.filterColor != null) {
            augUrl += "&imgc=" + filter.filterColor;
        }

        if (filter.filterType != null) {
            augUrl += "&imgtype=" + filter.filterType;
        }

        if (filter.filterSite != null) {
            augUrl += "&as_sitesearch=" + filter.filterSite;
        }

        return augUrl;
    }

    public void showFilters(MenuItem item) {
        Intent intent = new Intent(SearchActivity.this, FilterActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            filter = (Filter) data.getExtras().getSerializable("filter");

        }
    }
}
