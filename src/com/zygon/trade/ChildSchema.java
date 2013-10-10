package com.zygon.trade;

/**
 *
 * @author zygon
 */
public class ChildSchema extends Schema {

    private final String childCls;

    public ChildSchema(String childCls, Property[] properties) {
        super(properties);

        this.childCls = childCls;
    }

    public String getChildCls() {
        return this.childCls;
    }
}
