public class LiftRide {
    private Integer skierID;
    private Integer resortID;
    private Integer seasonID;
    private Integer dayID;
    private Integer time;
    private Integer liftID;

    public LiftRide(Integer skierID, Integer resortID, Integer seasonID, Integer dayID, Integer time, Integer liftID) {
        this.skierID = skierID;
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.time = time;
        this.liftID = liftID;
    }

    public Integer getSkierID() {
        return skierID;
    }

    public void setSkierID(Integer skierID) {
        this.skierID = skierID;
    }

    public Integer getResortID() {
        return resortID;
    }

    public void setResortID(Integer resortID) {
        this.resortID = resortID;
    }

    public Integer getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(Integer seasonID) {
        this.seasonID = seasonID;
    }

    public Integer getDayID() {
        return dayID;
    }

    public void setDayID(Integer dayID) {
        this.dayID = dayID;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getLiftId() {
        return liftID;
    }

    public void setLiftId(Integer liftId) {
        this.liftID = liftId;
    }
}
