package com.zygon.trade;

import com.zygon.schema.NumericSchemaElement;
import com.zygon.schema.PropertiesSchemaElement;
import com.zygon.schema.SchemaElement;
import com.zygon.schema.Type;
import static com.zygon.schema.Type.ARRAY;
import static com.zygon.schema.Type.INTEGER;
import static com.zygon.schema.Type.NUMBER;
import static com.zygon.schema.Type.OBJECT;
import static com.zygon.schema.Type.STRING;

/**
 *
 * @author zygon
 */
public class SchemaRenderer {

    private static final String FMT = "%s[%s - %s] (%s)"; // padding, id, description, type
    
    // TBD: an output controller
    public void render(StringBuilder sb, SchemaElement element) {
        
        if (element.getType() != Type.OBJECT && element.getType() != Type.ARRAY) {
            sb.append(String.format(FMT, "  ", element.getId(), element.getDescription(), element.getType().name()));
        }
        
        String restrictions = null;
        
        switch (element.getType()) {
            case ARRAY:
                throw new UnsupportedOperationException();
            case INTEGER:
            case NUMBER:  // fall through
                NumericSchemaElement numElem = (NumericSchemaElement) element;
                StringBuilder iSB = new StringBuilder();
                iSB.append("[");
                if (numElem.getMin() != null) {
                    iSB.append(numElem.getMin());
                } else {
                    iSB.append("*");
                }
                
                iSB.append("-");
                
                if (numElem.getMax() != null) {
                    iSB.append(numElem.getMax());
                } else {
                    iSB.append("*");
                }
                
                if (numElem.isExclusiveMinimum()) {
                    iSB.append(" exclusive");
                }
                
                iSB.append("]");
                
                restrictions = iSB.toString();
                break;
            case OBJECT:
                PropertiesSchemaElement objElem = (PropertiesSchemaElement) element;
                
                for (int i = 0; i < objElem.getElements().length; i++) {
                    sb.append("\n");
                    this.render(sb, objElem.getElements()[i]);
                }
                
                break;
            case STRING:
                // TBD: regexes, etc.
                break;
            default:
                throw new UnsupportedOperationException();
        }
        
        if (restrictions != null) {
            sb.append(" ").append(restrictions);
        }
    }
}
