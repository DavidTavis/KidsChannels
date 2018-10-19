package kids.youtube.channels.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourceId {

    @SerializedName("videoId")
    @Expose
    public String videoId;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResourceId() {
    }

    /**
     *
     * @param videoId
     */
    public ResourceId(String videoId) {
        super();
        this.videoId = videoId;
    }

}