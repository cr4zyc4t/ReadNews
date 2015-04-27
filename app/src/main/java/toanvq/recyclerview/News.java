package toanvq.recyclerview;

import java.io.Serializable;

/**
 * Created by Admin on 2015-04-24.
 */
public class News implements Serializable {
    private String title, icon, time, description;
    private int id;

    public News(String title, String icon, String time, String description, int id) {
        this.title = title;
        this.icon = icon;
        this.time = time;
        this.description = description;
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
