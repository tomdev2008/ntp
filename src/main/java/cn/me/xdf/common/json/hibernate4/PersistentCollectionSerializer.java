package cn.me.xdf.common.json.hibernate4;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.*;
import org.hibernate.collection.spi.PersistentCollection;

import javax.persistence.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午10:50
 * To change this template use File | Settings | File Templates.
 */
public class PersistentCollectionSerializer
        extends JsonSerializer<Object>
        implements ContextualSerializer {

    protected final boolean _forceLazyLoading;

    protected final JsonSerializer<Object> _serializer;


    @SuppressWarnings("unchecked")
    public PersistentCollectionSerializer(boolean forceLazyLoading,
                                          JsonSerializer<?> serializer) {
        _forceLazyLoading = forceLazyLoading;
        _serializer = (JsonSerializer<Object>) serializer;
    }



    /*
    /**********************************************************************
    /* JsonSerializer impl
    /**********************************************************************
     */

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        if (value instanceof PersistentCollection) {
            PersistentCollection coll = (PersistentCollection) value;
            if (!_forceLazyLoading && !coll.wasInitialized()) {
                provider.defaultSerializeNull(jgen);
                return;
            }
            value = coll.getValue();
            if (value == null) {
                provider.defaultSerializeNull(jgen);
                return;
            }
        }
        if (_serializer == null) { // sanity check...
            throw new JsonMappingException("PersistentCollection does not have serializer set");
        }
        _serializer.serialize(value, jgen, provider);
    }

    @Override
    public void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider,
                                  TypeSerializer typeSer)
            throws IOException, JsonProcessingException {
        if (value instanceof PersistentCollection) {
            PersistentCollection coll = (PersistentCollection) value;
            if (!_forceLazyLoading && !coll.wasInitialized()) {
                provider.defaultSerializeNull(jgen);
                return;
            }
            value = coll.getValue();
            if (value == null) {
                provider.defaultSerializeNull(jgen);
                return;
            }
        }
        if (_serializer == null) { // sanity check...
            throw new JsonMappingException("PersistentCollection does not have serializer set");
        }
        _serializer.serializeWithType(value, jgen, provider, typeSer);
    }

    /*
    /**********************************************************************
    /* Helper methods
    /**********************************************************************
     */
    protected boolean usesLazyLoading(BeanProperty property) {
        if (property != null) {
            ElementCollection ec = property.getAnnotation(ElementCollection.class);
            if (ec != null) {
                return (ec.fetch() == FetchType.LAZY);
            }
            OneToMany ann1 = property.getAnnotation(OneToMany.class);
            if (ann1 != null) {
                return (ann1.fetch() == FetchType.LAZY);
            }
            OneToOne ann2 = property.getAnnotation(OneToOne.class);
            if (ann2 != null) {
                return (ann2.fetch() == FetchType.LAZY);
            }
            ManyToOne ann3 = property.getAnnotation(ManyToOne.class);
            if (ann3 != null) {
                return (ann3.fetch() == FetchType.LAZY);
            }
            ManyToMany ann4 = property.getAnnotation(ManyToMany.class);
            if (ann4 != null) {
                return (ann4.fetch() == FetchType.LAZY);
            }
        }
        return false;
    }

    @Override
    public JsonSerializer createContextual(SerializationConfig config, BeanProperty property) throws JsonMappingException {
        JsonSerializer<?> ser = handlePrimaryContextualization(config, _serializer, property);


        if (_forceLazyLoading || !usesLazyLoading(property)) {
            return ser;
        }
        if (ser != _serializer) {
            return new PersistentCollectionSerializer(_forceLazyLoading, ser);
        }
        return this;
    }

    public JsonSerializer<?> handlePrimaryContextualization(SerializationConfig config, JsonSerializer<?> ser,
                                                            BeanProperty property)
            throws JsonMappingException {
        if (ser != null) {
            if (ser instanceof ContextualSerializer) {
                ser = ((ContextualSerializer) ser).createContextual(config, property);
            }
        }
        return ser;
    }


}