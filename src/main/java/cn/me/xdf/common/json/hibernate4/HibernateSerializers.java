package cn.me.xdf.common.json.hibernate4;

import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.type.*;
import org.codehaus.jackson.type.JavaType;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.proxy.HibernateProxy;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
public class HibernateSerializers extends Serializers.Base
{
    protected final boolean _forceLoading;
    protected final boolean _serializeIdentifiers;
    protected final Mapping _mapping;

    public HibernateSerializers(boolean forceLoading)
    {
        this(forceLoading, false, null);
    }

    public HibernateSerializers(boolean forceLoading, boolean serializeIdentifiers)
    {
        this(forceLoading, serializeIdentifiers, null);
    }

    public HibernateSerializers(boolean forceLoading, boolean serializeIdentifiers, Mapping mapping)
    {
        _forceLoading = forceLoading;
        _serializeIdentifiers = serializeIdentifiers;
        _mapping = mapping;
    }


    public JsonSerializer<?> findSerializer(SerializationConfig config,
                                            JavaType type, BeanDescription beanDesc)
    {
        Class<?> raw = type.getRawClass();
        if (HibernateProxy.class.isAssignableFrom(raw)) {
            return new HibernateProxySerializer(_forceLoading, _serializeIdentifiers, _mapping);
        }
        return null;
    }
}