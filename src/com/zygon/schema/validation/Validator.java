
package com.zygon.schema.validation;

import com.zygon.trade.Schema;

/**
 *
 * @author zygon
 */
public interface Validator {
    public ValidationResult validate (Schema schema);
}
