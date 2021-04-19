import static java.lang.Math.*;

class ThreadF extends my_Thread {
    public ThreadF(String name_, Function res) {
        super(name_, res);
    }

    protected void findValue() throws InterruptedException {
        res.set((float) (pow(sin(x),2)+pow(cos(x),2)));
        sleep(4000);
    }
}