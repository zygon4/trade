
package com.zygon.trade;

/**
 *
 * @author zygon
 */
public class Response {

    private final String output;

    public Response(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return this.getOutput();
    }
}
