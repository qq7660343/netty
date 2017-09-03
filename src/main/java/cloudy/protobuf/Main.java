package cloudy.protobuf;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 7cc on 2017/9/3
 */
public class Main {

    public static volatile boolean running = true;

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        try {
            context.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                context.stop();
                running = false;
                Main.class.notify();
            }));

            synchronized (Main.class) {
                while(running){
                    try {
                        Main.class.wait();
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

}
