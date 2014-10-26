
package com.zygon.trade.ui.web;

/**
 *
 * @author zygon
 */
public class WebUtil {

    public static String writeTag(String tag, String id, String clazz, String content) {
        
        if (tag == null) {
            throw new IllegalArgumentException("Must provide tag");
        }
        
        if (content == null) {
            throw new IllegalArgumentException("Must provide content");
        }
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("<").append(tag);
        
        if (id != null) {
            sb.append(" id=\"").append(id).append("\"");
        }
        
        if (clazz != null) {
            sb.append(" class=\"").append(clazz).append("\"");
        }
        
        sb.append(">");
        sb.append("\n");
        
        sb.append(content);
        sb.append("\n");
        
        sb.append("</").append(tag).append(">");
        
        return sb.toString();
    }
    
    public static String writeTag(String tag, String id, String content) {
        return writeTag(tag, id, null, content);
    }
    
    public static String writeTag(String tag, String content) {
        return writeTag(tag, null, content);
    }
}
