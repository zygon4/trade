/**
 * 
 */

package com.zygon.exchange.message;

/**
 *
 * @author zygon
 */
public class OctetString {

    // TODO: wire encoding/decoding
    
    private final byte[] data;
    private final int offset;
    private final int length;

    public OctetString(byte[] data, int offset, int length) {
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    protected byte[] getData() {
        return data;
    }

    protected int getLength() {
        return length;
    }

    protected int getOffset() {
        return offset;
    }
}
