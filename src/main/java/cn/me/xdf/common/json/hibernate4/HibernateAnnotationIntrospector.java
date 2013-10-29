package cn.me.xdf.common.json.hibernate4;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.impl.Indenter;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.*;
import org.codehaus.jackson.type.JavaType;

import javax.persistence.Transient;
import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
public class HibernateAnnotationIntrospector extends AnnotationIntrospector
{
    private static final long serialVersionUID = 1L;

    /**
     * Whether we should check for existence of @Transient or not.
     * Default value is 'true'.
     */
    protected boolean _cfgCheckTransient;

    /*
    /**********************************************************************
    /* Construction, configuration
    /**********************************************************************
     */

    public HibernateAnnotationIntrospector() { }





    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return _cfgCheckTransient && c.hasAnnotation(Transient.class);
    }

    public boolean isIgnorableField(AnnotatedField f) {
        return _cfgCheckTransient && f.hasAnnotation(Transient.class);
    }

    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return _cfgCheckTransient && m.hasAnnotation(Transient.class);
    }


    /*
    /**********************************************************************
    /*  implementation/overrides
    /**********************************************************************
    */

    @Override
    public boolean isHandled(Annotation ann) {
        return false;
    }

    @Override
    public String findRootName(AnnotatedClass ac) {
        return null;
    }

    @Override
    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        return null;
    }

    @Override
    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        return null;
    }


    public HibernateAnnotationIntrospector setUseTransient(boolean state) {
        _cfgCheckTransient = state;
        return this;
    }


    @Override
    public Object findSerializer(Annotated am) {
        return null;
    }

    @Override
    public Class<?> findSerializationType(Annotated a) {
        return null;
    }

    @Override
    public JsonSerialize.Typing findSerializationTyping(Annotated a) {
        return null;
    }

    @Override
    public Class<?>[] findSerializationViews(Annotated a) {
        return null;
    }

    @Override
    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        return null;
    }

    @Override
    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        return null;
    }

    @Override
    public String findGettablePropertyName(AnnotatedMethod am) {
        return null;
    }

    @Override
    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        return false;
    }

    @Override
    public String findEnumValue(Enum<?> value) {
        return null;
    }

    @Override
    public String findSerializablePropertyName(AnnotatedField af) {
        return null;
    }

    @Override
    public Object findDeserializer(Annotated am) {
        return null;
    }

    @Override
    public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated am) {
        return null;
    }

    @Override
    public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated am) {
        return null;
    }

    @Override
    public Class<?> findDeserializationType(Annotated am, JavaType baseType, String propName) {
        return null;
    }

    @Override
    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
        return null;
    }

    @Override
    public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType, String propName) {
        return null;
    }

    @Override
    public String findSettablePropertyName(AnnotatedMethod am) {
        return null;
    }

    @Override
    public String findDeserializablePropertyName(AnnotatedField af) {
        return null;
    }

    @Override
    public String findPropertyNameForParam(AnnotatedParameter param) {
        return null;
    }
}