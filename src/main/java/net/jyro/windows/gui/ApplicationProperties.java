package net.jyro.windows.gui;

public class ApplicationProperties {
    private String title;
    private int width;
    private int height;
    public ApplicationProperties(String title,int width,int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getApplicationTitle() {
        return title;
    }

    public int getConcurrentApplicationWidth() {
        return width;
    }

    public int getConcurrentApplicationHeight() {
        return height;
    }
}
