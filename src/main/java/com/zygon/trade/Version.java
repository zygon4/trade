
package com.zygon.trade;

import com.google.common.base.Preconditions;

/**
 *
 * @author zygon
 */
public class Version {
    
    private final int major;
    private final int minor;
    private final int point;
    private final String display;

    public Version(int major, int minor, int point) {
        Preconditions.checkArgument(major >= 0);
        Preconditions.checkArgument(minor >= 0);
        Preconditions.checkArgument(point >= 0);
        
        this.major = major;
        this.minor = minor;
        this.point = point;
        this.display = String.format("%d.%d.%d", this.major, this.minor, this.point);
    }
    
    public String getDisplay() {
        return this.display;
    }

    public final int getMajor() {
        return this.major;
    }

    public final int getMinor() {
        return this.minor;
    }

    public final int getPoint() {
        return this.point;
    }

    @Override
    public String toString() {
        return this.display;
    }
}
