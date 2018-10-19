
package kids.youtube.channels.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medium {

    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("width")
    @Expose
    public int width;
    @SerializedName("height")
    @Expose
    public int height;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Medium() {
    }

    /**
     * 
     * @param height
     * @param width
     * @param url
     */
    public Medium(String url, int width, int height) {
        super();
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Medium{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
