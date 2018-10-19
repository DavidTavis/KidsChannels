
package kids.youtube.channels.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Id {

    public String channelId;


    public Id() {
    }

    public Id(String channelId) {
        super();
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "Id{" +
                "channelId='" + channelId + '\'' +
                '}';
    }
}
