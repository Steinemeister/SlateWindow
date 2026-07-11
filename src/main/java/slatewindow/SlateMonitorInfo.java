package slatewindow;

public class SlateMonitorInfo {
    private final String name;
    private final int width;
    private final int height;
    private final int refreshRate;

    public SlateMonitorInfo(String name, int width, int height, int refreshRate) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.refreshRate = refreshRate;
    }

    public String getName() { return name; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getRefreshRate() { return refreshRate; }
}

