import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " thread started");
        float x = Functional.getX();
        Function f_ = new Function(), g_ = new Function();
        ThreadF f = new ThreadF("F", f_);
        ThreadG g = new ThreadG("G", g_);

        f.setX(x); g.setX(x);
        f.start(); g.start();

        boolean flag = true, error = false;
        int curtime = 0, wakeup = 1000, step = 100;
        try{
            while (flag){
                if(!f.isAlive() && !g.isAlive())
                    flag = false;
                if(curtime >= wakeup){
                    if(f.isAlive()){
                        if(!Functional.Exec("F")) {
                            f.interrupt();
                            error = true;
                        }
                    }
                    if(g.isAlive()){
                        if(!Functional.Exec("G")) {
                            g.interrupt();
                            error = true;
                        }
                    }
                    curtime = 0;
                }
                Thread.sleep(step);
                curtime += step;
            }

            if(!error) {
                System.out.println("f(x) = " + f_.get());
                System.out.println("g(x) = " + g_.get());
                System.out.println("f(x)*g(x) = " + f_.get() * g_.get());
            } else {
                System.out.println("Something went wrong (interrupted process)");
            }
        }
        catch(InterruptedException | IOException e){
            System.out.println(e.getCause());
        }
        System.out.println(Thread.currentThread().getName() + " thread finished");
    }
}

