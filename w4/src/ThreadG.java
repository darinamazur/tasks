import static java.lang.Math.*;


class ThreadG extends my_Thread {
    public ThreadG(String name_, Function res) {
        super(name_, res);
    }

    protected void findValue() throws InterruptedException {
        //res.set((float) exp(pow(x,2)));
        res.set((float) (exp(pow(x,2))*(1/tan(0))));
        sleep(3000);
    }
}