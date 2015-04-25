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
public class RecyclerView_Adapter extends RecyclerView.Adapter<RecyclerView_Adapter.View_Holder> {

    private List<RecyclerView_Item> itemList;
    private Context mContext;
    private ItemClickListener itemClickListener;

    public RecyclerView_Adapter(List<RecyclerView_Item> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        final View_Holder view_holder = new View_Holder(view);

        view_holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null){
                    itemClickListener.ItemClicked(view_holder.getAdapterPosition());
                }
            }
        });

        return view_holder;
    }

    @Override
    public void onBindViewHolder(final View_Holder view_holder, int i) {
        RecyclerView_Item item = itemList.get(i);

        view_holder.title.setText(item.getTitle());

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(item.getIcon(),new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null){
                    view_holder.icon.setImageBitmap( Utils.getRoundedShape(response.getBitmap(),0,0));
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return (itemList != null ? itemList.size() : 0);
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        protected ImageView icon;
        protected TextView title;
        protected View parent;

        public View_Holder(View itemView) {
            super(itemView);
            this.parent = itemView;
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public interface ItemClickListener{
        void ItemClicked(int position);
    }
}
