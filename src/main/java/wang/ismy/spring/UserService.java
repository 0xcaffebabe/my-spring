package wang.ismy.spring;

import wang.ismy.spring.annotation.Autowired;
import wang.ismy.spring.annotation.Service;

/**
 * @author MY
 * @date 2020/1/14 9:33
 */
@Service
public class UserService {

    @Autowired
    private OrderService orderService;

    public void run(){
        if (orderService != null){
            orderService.run();
        }
        System.out.println("user service");
    }

    public OrderService getOrderService() {
        return orderService;
    }
}
