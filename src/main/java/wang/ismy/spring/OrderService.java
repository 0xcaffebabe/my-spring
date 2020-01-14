package wang.ismy.spring;

import wang.ismy.spring.annotation.Service;

/**
 * @author MY
 * @date 2020/1/14 10:35
 */
@Service
public class OrderService {
    public void run(){
        System.out.println("order service");
    }
}
