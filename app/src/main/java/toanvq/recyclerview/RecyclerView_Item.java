package toanvq.recyclerview;

/**
 * Created by Admin on 2015-04-24.
 */
public class RecyclerView_Item {
    private String title, icon;
    private int server_id;

    public RecyclerView_Item(String title, String icon, int server_id) {
        this.title = title;
        this.icon = icon;
        this.server_id = server_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }
}
