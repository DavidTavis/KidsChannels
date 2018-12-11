
package kids.youtube.channels.model.dto;

public class Id {

    public String channelId;
    public String videoId;


    public Id() {
    }

    public Id(String channelId, String videoId) {
        super();
        this.channelId = channelId;
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return "Id{" +
                "channelId='" + channelId + '\'' +
                "videoId='" + videoId + '\'' +
                '}';
    }
}
