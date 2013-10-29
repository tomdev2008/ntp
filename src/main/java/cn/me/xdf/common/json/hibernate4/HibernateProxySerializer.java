package cn.me.xdf.common.json.hibernate4;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午10:27
 * To change this template use File | Settings | File Templates.
 */
public class HibernateProxySerializer
        extends JsonSerializer<HibernateProxy>
{

    protected final BeanProperty _property;

    protected final boolean _forceLazyLoading;
    protected final boolean _serializeIdentifier;
    protected final Mapping _mapping;


    protected PropertySerializerMap _dynamicSerializers;



    public HibernateProxySerializer(boolean forceLazyLoading)
    {
        this(forceLazyLoading, false, null);
    }

    public HibernateProxySerializer(boolean forceLazyLoading, boolean serializeIdentifier) {
        this(forceLazyLoading, serializeIdentifier, null);
    }

    public HibernateProxySerializer(boolean forceLazyLoading, boolean serializeIdentifier, Mapping mapping) {
        _forceLazyLoading = forceLazyLoading;
        _serializeIdentifier = serializeIdentifier;
        _mapping = mapping;
        _dynamicSerializers = PropertySerializerMap.emptyMap();
        _property = null;
    }



    @Override
    public void serialize(HibernateProxy value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        Object proxiedValue = findProxied(value);
        // TODO: figure out how to suppress nulls, if necessary? (too late for that here)
        if (proxiedValue == null) {
            provider.defaultSerializeNull(jgen);
            return;
        }
        findSerializer(provider, proxiedValue).serialize(proxiedValue, jgen, provider);
    }

    @Override
    public void serializeWithType(HibernateProxy value, JsonGenerator jgen, SerializerProvider provider,
                                  TypeSerializer typeSer)
            throws IOException, JsonProcessingException
    {
        Object proxiedValue = findProxied(value);
        if (proxiedValue == null) {
            provider.defaultSerializeNull(jgen);
            return;
        }
        /* This isn't exactly right, since type serializer really refers to proxy
         * object, not value. And we really don't either know static type (necessary
         * to know how to apply additional type info) or other things;
         * so it's not going to work well. But... we'll do out best.
         */
        findSerializer(provider, proxiedValue).serializeWithType(proxiedValue, jgen, provider, typeSer);
    }



    protected JsonSerializer<Object> findSerializer(SerializerProvider provider, Object value)
            throws IOException, JsonProcessingException
    {
        Class<?> type = value.getClass();

        PropertySerializerMap.SerializerAndMapResult result =
                _dynamicSerializers.findAndAddSerializer(type, provider, _property);
        if (_dynamicSerializers != result.map) {
            _dynamicSerializers = result.map;
        }
        return result.serializer;
    }

    @SuppressWarnings("serial")
    protected Object findProxied(HibernateProxy proxy)
    {
        LazyInitializer init = proxy.getHibernateLazyInitializer();
        if (!_forceLazyLoading && init.isUninitialized()) {
            if(_serializeIdentifier){
                final String idName;
                if (_mapping != null) {
                    idName = _mapping.getIdentifierPropertyName(init.getEntityName());
                } else {
                    final SessionImplementor session = init.getSession();
                    if (session != null) {
                        idName = session.getFactory().getIdentifierPropertyName(init.getEntityName());
                    } else {
                        idName = init.getEntityName();
                    }
                }
                final Object idValue = init.getIdentifier();
                return new HashMap<String, Object>(){{ put(idName, idValue); }};
            } else {
                return null;
            }
        }
        return init.getImplementation();
    }
}