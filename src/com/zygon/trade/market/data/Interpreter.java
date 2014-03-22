
package com.zygon.trade.market.data;

import com.zygon.trade.market.model.indication.Indication;

/**
 * Responsible for (optionally) interpreting incoming data into another
 * form.  This could be time based aggregation, filtering, etc.
 */
public interface Interpreter<T> {
    public Indication[] interpret(T data);
}
