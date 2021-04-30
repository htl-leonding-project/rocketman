package at.htl.rocketman.entity;

public class CanSatConfiguration {
    private String name;
    private int countdown;
    private int igniter;
    private String resistance;
    private boolean useJoyStick;
    private boolean useVideo;

    public CanSatConfiguration(String name, int countdown, int igniter, String resistance, boolean useJoyStick, boolean useVideo) {
        this.name = name;
        this.countdown = countdown;
        this.igniter = igniter;
        this.resistance = resistance;
        this.useJoyStick = useJoyStick;
        this.useVideo = useVideo;
    }

    public CanSatConfiguration() {
        this("", -1, -1, "", false, false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public int getIgniter() {
        return igniter;
    }

    public void setIgniter(int igniter) {
        this.igniter = igniter;
    }

    public String getResistance() {
        return resistance;
    }

    public void setResistance(String resistance) {
        this.resistance = resistance;
    }

    public boolean isUseJoyStick() {
        return useJoyStick;
    }

    public void setUseJoyStick(boolean useJoyStick) {
        this.useJoyStick = useJoyStick;
    }

    public boolean isUseVideo() {
        return useVideo;
    }

    public void setUseVideo(boolean useVideo) {
        this.useVideo = useVideo;
    }
}
