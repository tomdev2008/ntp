package com.sun.xml.txw2.output;

import com.sun.xml.txw2.TypedXmlWriter;

import javax.xml.transform.Result;

/**
 * Allow you to wrap {@link com.sun.xml.txw2.TypedXmlWriter} into a {@link javax.xml.transform.Result}
 * so that it can be passed to {@link com.sun.xml.txw2.output.ResultFactory}.
 *
 * <p>
 * This class doesn't extend from known {@link javax.xml.transform.Result} type,
 * so it won't work elsewhere.
 *
 * @author Kohsuke Kawaguchi
 */
public class TXWResult implements Result {
    private String systemId;

    private TypedXmlWriter writer;

    public TXWResult(TypedXmlWriter writer) {
        this.writer = writer;
    }

    public TypedXmlWriter getWriter() {
        return writer;
    }

    public void setWriter(TypedXmlWriter writer) {
        this.writer = writer;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
