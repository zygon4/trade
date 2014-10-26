
package com.zygon.trade.ui.web;

/**
 *
 * @author zygon
 */
public class Styling {

    public static enum Float {
        RIGHT("right"),
        LEFT("left");

        private Float(String text) {
            this.text = text;
        }
        
        private final String text;

        public String getText() {
            return this.text;
        }
    }
    
    private Float floating = null;
    private Height height = null;
    private Width width = null;
    private String clazz = null;

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setFloating(Float floating) {
        this.floating = floating;
    }

    public Float getFloating() {
        return this.floating;
    }

    public Height getHeight() {
        return this.height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public Width getWidth() {
        return this.width;
    }

    public void setWidth(Width width) {
        this.width = width;
    }
    
    
}
