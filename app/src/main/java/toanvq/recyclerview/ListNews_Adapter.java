package toanvq.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import toanvq.recyclerview.volley.AppController;

/**
 * Created by Admin on 2015-04-24.
 */
public class ListNews_Adapter extends RecyclerView.Adapter<ListNews_Adapter.ViewHolder> {

    private List<News> listNews;
    private Context mContext;
    private NewsClickListener newsClickListener;

    public ListNews_Adapter(List<News> listNews, Context mContext) {
        this.listNews = listNews;
        this.mContext = mContext;
        setHasStableIds(true);
    }

    public NewsClickListener getNewsClickListener() {
        return newsClickListener;
    }

    public void setNewsClickListener(NewsClickListener newsClickListener) {
        this.newsClickListener = newsClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);
        final ViewHolder view_holder = new ViewHolder(view);

        view_holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newsClickListener != null){
                    newsClickListener.NewsClicked(view_holder.getAdapterPosition());
                }
            }
        });

        return view_holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder view_holder, int i) {
        News item = listNews.get(i);

        view_holder.title.setText(item.getTitle());
        view_holder.description.setText(item.getDescription());
        view_holder.timestamp.setText(item.getTime());

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get(item.getIcon(),new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null){
                    view_holder.icon.setImageBitmap( response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (listNews != null ? listNews.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return listNews.get(position).getId();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView icon;
        protected TextView title, description, timestamp ;
        protected View parent;

        public ViewHolder(View itemView) {
            super(itemView);
            this.parent = itemView;
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.description = (TextView) itemView.findViewById(R.id.description);
            this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }

        public View getIcon() {
            return icon;
        }

        public View getTitle() {
            return title;
        }

        public View getTimestamp() {
            return timestamp;
        }
    }

    public interface NewsClickListener{
        void NewsClicked(int position);
    }
}