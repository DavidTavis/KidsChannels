
package kids.youtube.channels.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Default {

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
    public Default() {
    }

    /**
     * 
     * @param height
     * @param width
     * @param url
     */
    public Default(String url, int width, int height) {
        super();
        this.url = url;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Default{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
