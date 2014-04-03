
package ctl.dev.api.service.object;

import com.google.gson.annotations.Expose;

public class Style_color {

    @Expose
    private String style;
    @Expose
    private String color;
    @Expose
    private String stylecolor;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStylecolor() {
        return stylecolor;
    }

    public void setStylecolor(String stylecolor) {
        this.stylecolor = stylecolor;
    }

}
