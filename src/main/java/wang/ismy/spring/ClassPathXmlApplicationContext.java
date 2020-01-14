package wang.ismy.spring;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 通过xml方式管理
 *
 * @author MY
 * @date 2020/1/14 9:03
 */
public class ClassPathXmlApplicationContext {

    private String xmlPath;
    private List<Element> elements;

    public ClassPathXmlApplicationContext(String xmlPath) {
        this.xmlPath = xmlPath;
        try {
            readXml();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("bean id 不能为空");
        }
        // 根据ID查找类信息初始化
        String klass = findById(id);
        if (StringUtils.isEmpty(klass)){
            throw new IllegalStateException("class is null");
        }
        try {
            return initBean(klass);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void readXml() throws DocumentException {
        // 读取根节点
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(this.getClass().getClassLoader().getResourceAsStream(xmlPath));
        Element root = doc.getRootElement();
        // 获取所有子节点
        elements = root.elements();
    }

    private Object initBean(String klass) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> aClass = Class.forName(klass);
        return aClass.getConstructor().newInstance();
    }

    private String findById(String id){
        for (Element e : elements) {
            String beanId = e.attributeValue("id");
            if (StringUtils.isEmpty(beanId)){
                continue;
            }
            if (id.equals(beanId)){
                return e.attributeValue("class");
            }
        }
        return null;
    }
}
