
package com.zygon.trade.ui.web;

/**
 *
 * @author zygon
 */
public class Size {

    public static enum Direction {
        HEIGHT ("height"),
        WIDTH ("width");
        
        private final String text;

        private Direction(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }
    
    public static enum ValueType {
        PCT ("pct"), // TODO: pct style??
        PX ("px");

        private ValueType(String text) {
            this.text = text;
        }
        
        private final String text;

        public String getText() {
            return this.text;
        }
    }
    
    private final Direction direction;
    private final ValueType type;
    private final double value;

    public Size(Direction direction, ValueType type, double value) {
        this.direction = direction;
        this.type = type;
        this.value = value;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public ValueType getType() {
        return this.type;
    }

    public double getValue() {
        return this.value;
    }
}
