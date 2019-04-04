package listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by zhu on 2019/4/4.
 */
public class ApplicationListenerTest implements ApplicationListener {


    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEvent) {
            System.out.println("receive application event");
        }
    }
}
