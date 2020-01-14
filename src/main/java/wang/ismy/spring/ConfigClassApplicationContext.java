package wang.ismy.spring;

import org.apache.commons.lang3.StringUtils;
import wang.ismy.spring.annotation.Autowired;
import wang.ismy.spring.annotation.Service;
import wang.ismy.spring.utils.ClassUtil;

import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过配置类注解管理
 * @author MY
 * @date 2020/1/14 9:57
 */
public class ConfigClassApplicationContext {
    private String packageName;
    private Map<String,Object> beans = new ConcurrentHashMap<>();
    private Map<String,Class<?>> classMap = new ConcurrentHashMap<>();

    public ConfigClassApplicationContext(Class<?> klass) {
        this.packageName = klass.getPackageName();
        initBean();
    }

    public Object getBean(String id){
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id 不能为空");
        }
        if (beans.containsKey(id)){
            return beans.get(id);
        }
        try {
            return newInstance(classMap.get(id));
        }catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private void initBean(){
        // 扫包获取包下所有类
        List<Class<?>> classes = ClassUtil.getClasses(packageName);
        // 找到存在注解的类
        findClassExistAnnotation(classes);
    }
    private Object newInstance(Class<?> klass) throws Exception {
        Object obj = klass.getConstructor().newInstance();
        // 获取当前类所有存在注解的属性
        List<Field> fields = findFieldExistAnnotation(klass);
        for (Field field : fields) {
            field.setAccessible(true);
            // 使用属性名称查找创建bean，并设值
            field.set(obj,getBean(field.getName()));
        }
        // 将创建的容器放入容器
        beans.put(toLowerCaseFirst(klass.getSimpleName()),obj);
        return obj;
    }

    private List<Field> findFieldExistAnnotation(Class<?> klass){
        Field[] fields = klass.getDeclaredFields();
        List<Field> ret = new ArrayList<>();
        for (Field field : fields) {
            if (field.getAnnotation(Autowired.class) != null){
                ret.add(field);
            }
        }
        return ret;
    }

    private void findClassExistAnnotation(List<Class<?>> classes){
        for (Class<?> aClass : classes) {
            if (aClass.getAnnotation(Service.class) != null){
                String simpleName = toLowerCaseFirst(aClass.getSimpleName());
                classMap.put(simpleName,aClass);
            }
        }
    }

    private String toLowerCaseFirst(String str){
        return str.substring(0,1).toLowerCase()+str.substring(1);
    }

}
