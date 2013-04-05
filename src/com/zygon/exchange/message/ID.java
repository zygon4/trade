/**
 * 
 */

package com.zygon.exchange.message;

/**
 *
 * 
 * 
 * @author zygon
 */
public final class ID extends OctetString implements Parameter {

//    public static final int ID = 0xA;
    
    public static final int LENGTH_TAG = 4;
    public static final int LENGTH_LENGTH = 4;
    
    public static final int OFFSET_TAG = 0;
    public static final int OFFSET_LENGTH = OFFSET_TAG + LENGTH_TAG;
    public static final int OFFSET_DATA = OFFSET_LENGTH + LENGTH_LENGTH;
    
    private final int tag;
    private final int dataOffset;
    private final int dataLength;
    
    public ID (byte[] data, int offset, int length) {
        super(data, offset, length);
        
        this.tag = getData()[getOffset() + OFFSET_TAG];
        this.dataOffset = getOffset() + OFFSET_DATA;
        this.dataLength = getLength() - (LENGTH_TAG + LENGTH_LENGTH);
    }
    
    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public int getDataLength() {
        return this.dataLength;
    }

    @Override
    public int getDataOffset() {
        return this.dataOffset;
    }
}
