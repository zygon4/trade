
package com.zygon.schema.validation;

import com.zygon.schema.SchemaElement;

/**
 *
 * @author zygon
 */
public class ValidationResult {
    private final SchemaElement element;
    private final String msg;

    public ValidationResult(SchemaElement element, String msg) {
        this.element = element;
        this.msg = msg;
    }

    public ValidationResult(SchemaElement element) {
        this(element, null);
    }

    public SchemaElement getElement() {
        return this.element;
    }

    public String getMsg() {
        return this.msg;
    }
    
    public boolean isFailure() {
        return this.getMsg() != null;
    }
}
