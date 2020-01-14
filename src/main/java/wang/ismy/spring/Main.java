package wang.ismy.spring;

import org.apache.commons.lang3.ClassPathUtils;

/**
 * @author MY
 * @date 2020/1/13 21:17
 */
public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        UserService userService = ((UserService) context.getBean("userService"));
        userService.run();
    }
}
