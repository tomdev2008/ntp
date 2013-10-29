package cn.me.xdf.common.json;

import java.io.IOException;

import cn.me.xdf.common.json.hibernate4.Hibernate4Module;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json工具类
 *
 * @author xiaobin
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper objectMapper = null;

    /**
     *    为了防止Hibernate的Lazy加载和代理对象出现HibernateException
     *  * @param entity
     * @return
     */
    public static String writeObjectToJsonWithHibernate(Object entity) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new Hibernate4Module());
            return mapper.writeValueAsString(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把对象转化为json
     *
     * @param entity
     * @return
     */
    public static String writeObjectToJson(Object entity) {
        try {
            if (objectMapper == null)
                objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把json字符串转化为Object
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T readObjectByJson(String json, Class<T> clazz) {
        try {
            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }
            return objectMapper.readValue(json, clazz);
        } catch (JsonParseException e) {
            log.error("json==" + json + ",error(JsonParseException):"
                    + e.getMessage());
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            log.error("json==" + json + ",error(JsonMappingExcption):"
                    + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("json==" + json + ",error(IOException):" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
