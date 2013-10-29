package cn.me.xdf.common.json.hibernate4;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.impl.Indenter;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.Module;
import org.hibernate.engine.spi.Mapping;

/**
 * Created with IntelliJ IDEA.
 * User: xiaobin268
 * Date: 13-10-29
 * Time: 上午10:10
 * To change this template use File | Settings | File Templates.
 * 为了兼容Hibernate一对多和多对一的关联建立的Json和Hibernate的桥联
 */
public class Hibernate4Module extends Module {


    /**
     * 枚举定义的所有功能模块集合
     */
    public enum Feature {

        FORCE_LAZY_LOADING(false),


        USE_TRANSIENT_ANNOTATION(true),


        SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS(false)
        ;

        final boolean _defaultState;
        final int _mask;


        public static int collectDefaults()
        {
            int flags = 0;
            for (Feature f : values()) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }

        private Feature(boolean defaultState) {
            _defaultState = defaultState;
            _mask = (1 << ordinal());
        }

        public boolean enabledByDefault() { return _defaultState; }
        public int getMask() { return _mask; }
    }

    protected final static int DEFAULT_FEATURES = Feature.collectDefaults();


    protected int _moduleFeatures = DEFAULT_FEATURES;


    protected final Mapping _mapping;



    public Hibernate4Module() {
        this(null);
    }

    public Hibernate4Module(Mapping mapping) {
        _mapping = mapping;
    }

    @Override public String getModuleName() { return "ntp"; }
    @Override public Version version() { return ModuleVersion.versionFor(Indenter.class);}

    @Override
    public void setupModule(SetupContext context)
    {

        AnnotationIntrospector ai = annotationIntrospector();
        if (ai != null) {
            context.appendAnnotationIntrospector(ai);
        }
        boolean forceLoading = isEnabled(Feature.FORCE_LAZY_LOADING);
        context.addSerializers(new HibernateSerializers(forceLoading, isEnabled(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS), _mapping));
        context.addBeanSerializerModifier(new HibernateSerializerModifier(forceLoading));
    }


    protected AnnotationIntrospector annotationIntrospector() {
        HibernateAnnotationIntrospector ai = new HibernateAnnotationIntrospector();
        ai.setUseTransient(isEnabled(Feature.USE_TRANSIENT_ANNOTATION));
        return ai;
    }

    public Hibernate4Module enable(Feature f) {
        _moduleFeatures |= f.getMask();
        return this;
    }

    public Hibernate4Module disable(Feature f) {
        _moduleFeatures &= ~f.getMask();
        return this;
    }

    public final boolean isEnabled(Feature f) {
        return (_moduleFeatures & f.getMask()) != 0;
    }

    public Hibernate4Module configure(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
        return this;
    }
}
