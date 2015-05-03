package toanvq.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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


public class SubCategoryActivity extends AppCompatActivity implements RecyclerView_Adapter.ItemClickListener {
    private final String FETCH_SUBCTG_URL = "http://content.amobi.vn/api/cafe24h/listsubcategory";

    private List<RecyclerView_Item> listSubCtg = new ArrayList<RecyclerView_Item>();
    private RecyclerView_Adapter adapter;
    private ProgressBar progressBar;
    private RecyclerView_Item category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        category = (RecyclerView_Item) getIntent().getSerializableExtra("category");

        RecyclerView subctg_container = (RecyclerView) findViewById(R.id.subctg_container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        subctg_container.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new RecyclerView_Adapter(listSubCtg, this);
        adapter.setItemClickListener(this);

        subctg_container.setAdapter(adapter);

        getSubCategory(category.getServer_id());

        // ACTIONBAR
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(category.getTitle());
    }

    private void getSubCategory(int category_id) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, FETCH_SUBCTG_URL + "?category_id=" + category_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray feeds = response.optJSONArray("list_subcategory");
                if (feeds != null) {
                    for (int i = 0; i < feeds.length(); i++) {
                        JSONObject feed = feeds.optJSONObject(i);
                        if (feed != null) {
                            RecyclerView_Item new_item = null;
                            int server_id = feed.optInt("id");
                            if (server_id != 0) {
                                new_item = new RecyclerView_Item(feed.optString("title"), feed.optString("icon"), server_id);
                                listSubCtg.add(new_item);
                                adapter.notifyItemInserted(listSubCtg.size() - 1);
                            }
                        }
                    }
                }

                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request, "GET_SUB_CTG");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove) {
            listSubCtg.remove(1);
            adapter.notifyItemRemoved(1);
            Log.d("List", "" + listSubCtg.size());
            return true;
        }
        if (id == R.id.action_add) {
//            getSubCategory(category.getServer_id());
            RecyclerView_Item new_item = new RecyclerView_Item("Added Item", "http://icons.iconarchive.com/icons/mazenl77/I-like-buttons-3a/512/Cute-Ball-Go-icon.png", listSubCtg.size());
            listSubCtg.add(1, new_item);
            adapter.notifyItemInserted(1);
            return true;
        }
        if (id == android.R.id.home){
            ActivityCompat.finishAfterTransition(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ItemClicked(int position) {
        RecyclerView_Item clicked_item = listSubCtg.get(position);
        Log.i("Click", "Click at " + clicked_item.getTitle());

        Intent  listnews = new Intent(SubCategoryActivity.this, ListNewsActivity.class);
        listnews.putExtra("subcategory", clicked_item);
        startActivity(listnews);
    }
}
