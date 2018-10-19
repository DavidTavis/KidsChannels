
package kids.youtube.channels.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Duration {

    @SerializedName("pageInfo")
    @Expose
    public PageInfo pageInfo;
    @SerializedName("items")
    @Expose
    public List<Item> items = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Duration() {
    }

    /**
     * 
     * @param items
     * @param pageInfo
     */
    public Duration(PageInfo pageInfo, List<Item> items) {
        super();
        this.pageInfo = pageInfo;
        this.items = items;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
