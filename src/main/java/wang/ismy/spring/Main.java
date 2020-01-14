package wang.ismy.spring;

import org.apache.commons.lang3.ClassPathUtils;

/**
 * @author MY
 * @date 2020/1/13 21:17
 */
public class Main {
    public static void main(String[] args) {
        // xml
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        UserService userService = ((UserService) context.getBean("userService"));
        userService.run();
        // 注解
        ConfigClassApplicationContext context1 = new ConfigClassApplicationContext(Main.class);
        UserService service = (UserService) context1.getBean("userService");
        System.out.println(service.getOrderService().equals(context1.getBean("orderService")));
        service.run();
    }
}
