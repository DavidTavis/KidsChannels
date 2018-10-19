
package kids.youtube.channels.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentDetails {

    @SerializedName("duration")
    @Expose
    public String duration;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContentDetails() {
    }

    /**
     * 
     * @param duration
     */
    public ContentDetails(String duration) {
        super();
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
