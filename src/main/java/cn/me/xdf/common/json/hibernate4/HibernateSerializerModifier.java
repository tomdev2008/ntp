package cn.me.xdf.common.json.hibernate4;

import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.ser.BeanSerializerModifier;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
public class HibernateSerializerModifier extends BeanSerializerModifier {
    protected final boolean _forceLoading;

    public HibernateSerializerModifier(boolean forceLoading) {
        _forceLoading = forceLoading;
    }



    public JsonSerializer<?> modifySerializer(SerializationConfig config,
                                              BasicBeanDescription beanDesc, JsonSerializer<?> serializer) {
        return new PersistentCollectionSerializer(_forceLoading, serializer);
    }
}
