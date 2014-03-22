
package com.zygon.trade.ui.web;

/**
 *
 * @author zygon
 */
public class WebUtil {

    public static String writeTag(String tag, String content) {
        return String.format("<%s>%s</%s>\n", tag, content, tag);
    }
}
