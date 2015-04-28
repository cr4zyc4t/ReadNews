package toanvq.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import toanvq.recyclerview.volley.AppController;


public class ListNewsActivity extends ActionBarActivity implements ListNews_Adapter.NewsClickListener {
    private final String FETCH_LIST_NEWS_URL = "http://content.amobi.vn/api/cafe24h/listcontent";
    private final int GRID_COLUMN = 2;
    private List<News> listNews = new ArrayList<News>();
    private ListNews_Adapter adapter;
    private ProgressBar progressBar;
    private RecyclerView listNews_view;
    private int currentColumnNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        RecyclerView_Item subcategory = (RecyclerView_Item) getIntent().getSerializableExtra("subcategory");

        ActionBar actionBar = getSupportActionBar();
        setTitle(subcategory.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listNews_view = (RecyclerView) findViewById(R.id.list_news_container);

        if (Utils.isTablet(getResources())) {
            listNews_view.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN));
        } else {
            listNews_view.setLayoutManager(new LinearLayoutManager(this));
        }

        adapter = new ListNews_Adapter(listNews, this);
        adapter.setNewsClickListener(this);
        listNews_view.setAdapter(adapter);

        getListNews(subcategory.getServer_id());
    }

    private void getListNews(int subcategory_id) {
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, FETCH_LIST_NEWS_URL + "?limit=10&subcategory_id=" + subcategory_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray feeds = response.optJSONArray("list_content");
                if (feeds != null) {
                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject feed = feeds.optJSONObject(i);
                        if (feed != null) {
                            News news = new News(feed.optString("title"), feed.optString("icon"), feed.optString("time"), feed.optString("description"), feed.optInt("id"));
                            listNews.add(news);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request, "GET_NEWS_REQUEST");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_news, menu);
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
        if (id == android.R.id.home){
            ActivityCompat.finishAfterTransition(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void NewsClicked(int position) {
        News clicked_item = listNews.get(position);
        Log.i("Action", " Click " + clicked_item.getTitle());

        Intent readnews = new Intent(ListNewsActivity.this, ReadActivity.class);
        readnews.putExtra("news", clicked_item);

        ListNews_Adapter.ViewHolder viewHolder = (ListNews_Adapter.ViewHolder) listNews_view.findViewHolderForItemId(clicked_item.getId());
        if ((viewHolder != null) && (! Utils.isTablet(this.getResources()))){
            Pair<View, String> titlePair = Pair.create(viewHolder.getTitle(), "title");
            Pair<View, String> timePair = Pair.create(viewHolder.getTimestamp(), "time");
            Pair<View, String> iconPair = Pair.create(viewHolder.getIcon(), "icon");
            Pair<View, String> sourcePair = Pair.create(viewHolder.getSource_icon(), "source");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, titlePair, timePair, iconPair, sourcePair);
            ActivityCompat.startActivity(this, readnews, options.toBundle());
        }else{
            startActivity(readnews);
        }
    }

    private enum LayoutManageType {
        LINEAR_LAYOUT,
        GRIG_LAYOUT
    }
}
