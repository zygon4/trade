
package com.zygon.analysis.util;

import java.math.BigDecimal;

/**
 *
 * @author zygon
 */
public class OrderUtil {

    /**
     * Returns the fee from the trade volume * fee percent * price
     * @param feePct - fee percent e.g. 1.02
     * @param volume - the trade volume
     * @param price - the asset price
     * @return
     */
    public static BigDecimal calcFee(BigDecimal feePct, BigDecimal volume, BigDecimal price) {
        return volume.multiply(price).multiply(feePct.divide(BigDecimal.valueOf(100)));
    }

    public static void main(String[] args) {
        System.out.println(calcFee(BigDecimal.valueOf(0.21), BigDecimal.ONE, BigDecimal.valueOf(355.0)));
    }
}
