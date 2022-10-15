import io.swagger.client.model.LiftRide;

import java.util.Objects;

public class MyLiftRide {
    private LiftRide body;
    private Integer resortID;
    private String seasonID;
    private String dayID;
    private Integer skierID;

    public MyLiftRide(LiftRide body, Integer resortID, String seasonID, String dayID, Integer skierID) {
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.skierID = skierID;
        this.body = body;
    }

    public LiftRide getBody() {
        return body;
    }

    public void setBody(LiftRide body) {
        this.body = body;
    }

    public Integer getResortID() {
        return resortID;
    }

    public void setResortID(Integer resortID) {
        this.resortID = resortID;
    }

    public String getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(String seasonID) {
        this.seasonID = seasonID;
    }

    public String getDayID() {
        return dayID;
    }

    public void setDayID(String dayID) {
        this.dayID = dayID;
    }

    public Integer getSkierID() {
        return skierID;
    }

    public void setSkierID(Integer skierID) {
        this.skierID = skierID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyLiftRide that = (MyLiftRide) o;
        return body.equals(that.body) && resortID.equals(that.resortID) && seasonID.equals(that.seasonID) && dayID.equals(that.dayID) && skierID.equals(that.skierID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body, resortID, seasonID, dayID, skierID);
    }

    @Override
    public String toString() {
        return "client1.MyLiftRide{" +
                "body=" + body +
                ", resortID=" + resortID +
                ", seasonID='" + seasonID + '\'' +
                ", dayID='" + dayID + '\'' +
                ", skierID=" + skierID +
                '}';
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
