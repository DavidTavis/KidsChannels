
package kids.youtube.channels.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet {

    @SerializedName("publishedAt")
    @Expose
    public String publishedAt;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("thumbnails")
    @Expose
    public Thumbnails thumbnails;

    @SerializedName("resourceId")
    @Expose
    public ResourceId resourceId;

    public String channelTitle;
    public String liveBroadcastContent;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Snippet() {
    }

    public Snippet(String publishedAt, String title, Thumbnails thumbnails, ResourceId resourceId, String channelTitle, String liveBroadcastContent) {
        this.publishedAt = publishedAt;
        this.title = title;
        this.thumbnails = thumbnails;
        this.resourceId = resourceId;
        this.channelTitle = channelTitle;
        this.liveBroadcastContent = liveBroadcastContent;
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "publishedAt='" + publishedAt + '\'' +
                ", title='" + title + '\'' +
                ", thumbnails=" + thumbnails +
                ", resourceId=" + resourceId +
                ", channelTitle='" + channelTitle + '\'' +
                ", liveBroadcastContent='" + liveBroadcastContent + '\'' +
                '}';
    }
}
