package cn.me.xdf.common.hibernate4.dynamic;import java.io.*;import java.util.HashSet;import java.util.Iterator;import java.util.Map;import java.util.Set;import java.util.concurrent.ConcurrentHashMap;import org.apache.commons.lang3.Validate;import org.dom4j.Document;import org.dom4j.DocumentException;import org.dom4j.Element;import org.dom4j.io.SAXReader;import org.hibernate.InvalidMappingException;import org.hibernate.internal.util.xml.*;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.context.ResourceLoaderAware;import org.springframework.core.io.Resource;import org.springframework.core.io.ResourceLoader;import org.springframework.core.io.support.ResourcePatternResolver;import org.xml.sax.EntityResolver;import org.xml.sax.InputSource;import org.xml.sax.SAXException;/** * @author xiaobin */public class DefaultDynamicHibernateAssembleBuilder implements        DynamicHibernateAssembleBuilder, ResourceLoaderAware {    private static final Logger LOGGER = LoggerFactory            .getLogger(DefaultDynamicHibernateAssembleBuilder.class);    private Map<String, String> namedHQLQueries;    private Map<String, String> namedSQLQueries;    /**     * 资源文件路径     */    private String[] fileNames = new String[0];    private ResourceLoader resourceLoader;    /**     * 查询语句名称缓存，不允许重复     */    private Set<String> nameCache = new HashSet<String>();    /**     * 资源文件路径     *     * @param fileNames     */    public void setFileNames(String[] fileNames) {        this.fileNames = fileNames;    }    @Override    public Map<String, String> getNamedHQLQueries() {        return namedHQLQueries;    }    @Override    public Map<String, String> getNamedSQLQueries() {        return namedSQLQueries;    }    @Override    public void init() throws IOException {        namedHQLQueries = new ConcurrentHashMap<String, String>();        namedSQLQueries = new ConcurrentHashMap<String, String>();        boolean flag = this.resourceLoader instanceof ResourcePatternResolver;        for (String file : fileNames) {            if (flag) {                Resource[] resources = ((ResourcePatternResolver) this.resourceLoader)                        .getResources(file);                buildMap(resources);            } else {                Resource resource = resourceLoader.getResource(file);                buildMap(resource);            }        }        // clear name cache        nameCache.clear();    }    public static void main(String[] args) throws IOException, DocumentException {        //classpath:cn/me/xdf/dao/dynamic/*-dynamic.xml        SAXReader saxReader = new SAXReader();        File file = new File("D:\\workspace\\idea\\ntp\\target\\otp-1.0.0-BUILD-SNAPSHOT\\WEB-INF\\classes\\dynamicsql\\user-dynamic.xml");        InputStream stream = new FileInputStream(file);        InputSource inputSource = new InputSource(stream);        Document document = saxReader.read(inputSource); //读取XML文件,获得document对象    }    @Override    public void setResourceLoader(ResourceLoader resourceLoader) {        this.resourceLoader = resourceLoader;    }    private void buildMap(Resource[] resources) throws IOException {        if (resources == null) {            return;        }        for (Resource resource : resources) {            buildMap(resource);        }    }    public XmlDocument readMappingDocument(InputSource source, Origin origin) {        SAXReader saxReader = new SAXReader();        Document document = null;        try {            // first try with orm 2.0 xsd validation            //setValidationFor(saxReader, "orm_2_0.xsd");            document = saxReader.read(source);            return new XmlDocumentImpl(document, origin.getType(), origin.getName());        } catch (Exception orm2Problem) {            orm2Problem.printStackTrace();        }        return null;    }    private void buildMap(Resource resource) {        InputSource inputSource = null;        try {            inputSource = new InputSource(resource.getInputStream());            XmlDocument metadataXml = readMappingDocument(inputSource,                    new OriginImpl("file", resource.getFilename()));            if (isDynamicStatementXml(metadataXml)) {                final Document doc = metadataXml.getDocumentTree();                final Element dynamicHibernateStatement = doc.getRootElement();                Iterator<?> rootChildren = dynamicHibernateStatement                        .elementIterator();                while (rootChildren.hasNext()) {                    final Element element = (Element) rootChildren.next();                    final String elementName = element.getName();                    if ("sql-query".equals(elementName)) {                        putStatementToCacheMap(resource, element,                                namedSQLQueries);                    } else if ("hql-query".equals(elementName)) {                        putStatementToCacheMap(resource, element,                                namedHQLQueries);                    }                }            }        } catch (Exception e) {            LOGGER.error(e.toString());            //throw new RuntimeException(e);            e.printStackTrace();        } finally {            if (inputSource != null && inputSource.getByteStream() != null) {                try {                    inputSource.getByteStream().close();                } catch (IOException e) {                    LOGGER.error(e.toString());                    throw new RuntimeException(e);                }            }        }    }    private void putStatementToCacheMap(Resource resource,                                        final Element element, Map<String, String> statementMap)            throws IOException {        String sqlQueryName = element.attribute("name").getText();        Validate.notEmpty(sqlQueryName);        if (nameCache.contains(sqlQueryName)) {            throw new RuntimeException("重复的sql-query/hql-query语句定义在文件:"                    + resource.getURI() + "中，必须保证name的唯一.");        }        nameCache.add(sqlQueryName);        String queryText = element.getText();        statementMap.put(sqlQueryName, queryText);    }    private static boolean isDynamicStatementXml(XmlDocument xmlDocument) {        return "dynamic-hibernate-statement".equals(xmlDocument                .getDocumentTree().getRootElement().getName());    }}