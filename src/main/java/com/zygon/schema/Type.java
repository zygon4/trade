
package com.zygon.schema;

/**
 *
 * @author zygon
 */
 public enum Type {
    ARRAY("array"), 
    INTEGER("integer"),
    NUMBER("number"), 
    OBJECT("object"), 
    STRING("string");
    
    private final String val;

    private Type(String val) {
        this.val = val;
    }

    public String getVal() {
        return this.val;
    }

    public static Type instance(String val) {
        for (Type type : Type.values()) {
            if (type.getVal().equals(val)) {
                return type;
            }
        }
        return null;
    }
}
