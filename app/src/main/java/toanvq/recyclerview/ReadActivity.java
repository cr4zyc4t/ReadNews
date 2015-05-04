package toanvq.recyclerview;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import toanvq.recyclerview.volley.AppController;


public class ReadActivity extends AppCompatActivity implements MyScrollView.OnScrollListener {
    private final String FETCH_NEWS_URL = "http://content.amobi.vn/api/cafe24h/contentdetail";

    ProgressBar progressBar;
    Button buttonRetry;
    WebView body;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#301976D2")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        final ImageView icon = (ImageView) findViewById(R.id.icon);
        ImageView source_icon = (ImageView) findViewById(R.id.profilePic);
        TextView title = (TextView) findViewById(R.id.title);
        TextView time = (TextView) findViewById(R.id.timestamp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonRetry = (Button) findViewById(R.id.buttonRetry);
        body = (WebView) findViewById(R.id.body);

        Log.i("Icon", "Width " + icon.getWidth());

        MyScrollView scrollView = (MyScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollListener(this);

        ViewCompat.setTransitionName(title, "title");
        ViewCompat.setTransitionName(time, "time");
        ViewCompat.setTransitionName(icon, "icon");
        ViewCompat.setTransitionName(source_icon, "source");

        final News news = (News) getIntent().getSerializableExtra("news");

        final int icon_width = Utils.getScreenWidth(this);
        Picasso.with(this).load(news.getIcon()).resize(icon_width, icon_width / 2).centerCrop().into(icon);
//        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//        imageLoader.get(news.getIcon(), new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                if (response.getBitmap() != null) {
//                    icon.setImageBitmap(Utils.resizeBitmap(response.getBitmap(), icon_width, icon_width/2 ));
//                }
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                    icon.setVisibility(View.GONE);
//            }
//        });

        title.setText(news.getTitle());
        time.setText(news.getTime());

//        getContentData(news.getId());
        new getContentAsync().execute(new String[]{"" + news.getId()});

        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentData(news.getId());
            }
        });
    }

    private void hideActionBar() {
        if (actionBar.isShowing()) {
            actionBar.hide();
        }
    }

    private void showActionBar() {
        if (!actionBar.isShowing()) {
            actionBar.show();
        }
    }

    private void getContentData(int id) {
        // TODO Auto-generated method stub
        progressBar.setVisibility(View.VISIBLE);
        buttonRetry.setVisibility(View.GONE);
        final String url = FETCH_NEWS_URL + "?content_id=" + id;
        Log.d("Link", url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response", response.toString());
                        try {
                            body.loadData(response.getJSONObject("content_detail").getString("content"), "text/html; charset=utf-8", "UTF-8");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Network Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                buttonRetry.setVisibility(View.VISIBLE);
            }
        }
        );

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
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
            if (getSupportActionBar().isShowing()) {
                getSupportActionBar().hide();
            } else {
                getSupportActionBar().show();
            }
            return true;
        }
        if (id == android.R.id.home) {
            ActivityCompat.finishAfterTransition(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrolled(int l, int t, int oldl, int oldt) {
//        if (t <= Utils.getActionBarHeight(this)) {
//            showActionBar();
//        } else {
//            if (t > oldt) { // SCROLL DOWN
//                hideActionBar();
//            } else {
//                if ((t < oldt) && (t > 0)) {
//                    showActionBar();
//                }
//            }
//        }
    }

    class getContentAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonRetry.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpGet request = new HttpGet(FETCH_NEWS_URL + "?content_id=" + params[0]);
//
//            try {
//                HttpResponse httpResponse = httpClient.execute(request);
//                InputStream inputStream = httpResponse.getEntity().getContent();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                StringBuilder stringBuilder = new StringBuilder();
//                String bufferedStrChunk = null;
//                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(bufferedStrChunk);
//                }
//                return stringBuilder.toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
            String url = FETCH_NEWS_URL + "?content_id=" + params[0];
            Log.d("Link", url);
            try {
                return Utils.StringRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject response = null;
            if (result != null) {
                try {
                    response = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    body.loadData(response.getJSONObject("content_detail").getString("content"), "text/html; charset=utf-8", "UTF-8");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                buttonRetry.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}
