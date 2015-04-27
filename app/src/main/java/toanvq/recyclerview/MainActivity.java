package toanvq.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import toanvq.recyclerview.volley.AppController;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements RecyclerView_Adapter.ItemClickListener {
        private static final String CONTENTS_URL = "http://content.amobi.vn/api/cafe24h/listcategory";
        private List<RecyclerView_Item> listItem = new ArrayList<RecyclerView_Item>();
        private RecyclerView_Adapter adapter;
        private ProgressBar progressBar;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_container);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new RecyclerView_Adapter(listItem, getActivity());
            adapter.setItemClickListener(this);

            recyclerView.setAdapter(adapter);

            getItems();

            return rootView;
        }

        private void getItems() {
            progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, CONTENTS_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONArray feeds = response.optJSONArray("list_category");
                    if (feeds != null) {
                        for (int i = 0; i < feeds.length(); i++) {
                            JSONObject feed = feeds.optJSONObject(i);
                            if (feed != null) {
                                RecyclerView_Item new_item = null;
                                int server_id = feed.optInt("id");
                                if (server_id != 0) {
                                    new_item = new RecyclerView_Item(feed.optString("title"), feed.optString("icon"), server_id);
                                    listItem.add(new_item);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Network Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(request, "GET_FEED");
        }

        @Override
        public void ItemClicked(int position) {
            RecyclerView_Item clicked_item = listItem.get(position);
            Log.i("Click", "Click at " + clicked_item.getTitle());

            Intent subcategory = new Intent(getActivity(), SubCategoryActivity.class);
//            subcategory.putExtra("category_id", clicked_item.getServer_id());
//            subcategory.putExtra("category_title", clicked_item.getTitle());
//            subcategory.putExtra("category_icon", clicked_item.getIcon());
            subcategory.putExtra("category", clicked_item);
            startActivity(subcategory);
        }
    }
}
