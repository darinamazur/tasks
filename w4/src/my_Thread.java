abstract class my_Thread extends Thread {

    float x;
    Function res = null;

    public my_Thread(){
        super();
    }

    public my_Thread(String name_, Function f){
        super(name_);
        res = f;
    }

    public void setX(float val){
        x = val;
    }

    protected abstract void findValue() throws InterruptedException;

    @Override
    public void run(){
        System.out.println(getName() + " started");
        while(!isInterrupted()){
            try{
                findValue();
                Thread.sleep(10);
                interrupt();
            }
            catch(InterruptedException e){
                System.out.println(getName() + " has been interrupted");
                interrupt();
            }
        }
        System.out.println(getName() + " finished");
    }
}