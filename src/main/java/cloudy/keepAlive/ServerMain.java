package cloudy.keepAlive;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 7cc on 2017/9/2
 */
public class ServerMain {

    public static volatile boolean running = true;

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        try {
            context.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                context.stop();
                running = false;
                ServerMain.class.notify();
            }));

            synchronized (ServerMain.class) {
                while (running) {
                    try {
                        ServerMain.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

}
