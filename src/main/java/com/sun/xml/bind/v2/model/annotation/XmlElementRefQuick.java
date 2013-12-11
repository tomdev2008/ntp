
package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElementRef;

final class XmlElementRefQuick
    extends Quick
    implements XmlElementRef
{

    private final XmlElementRef core;

    public XmlElementRefQuick(Locatable upstream, XmlElementRef core) {
        super(upstream);
        this.core = core;
    }

    protected Annotation getAnnotation() {
        return core;
    }

    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlElementRefQuick(upstream, ((XmlElementRef) core));
    }

    public Class<XmlElementRef> annotationType() {
        return XmlElementRef.class;
    }

    public String namespace() {
        return core.namespace();
    }

    public String name() {
        return core.name();
    }

    public Class type() {
        return core.type();
    }


	public boolean required() {
		// TODO Auto-generated method stub
		return false;
	}

}
