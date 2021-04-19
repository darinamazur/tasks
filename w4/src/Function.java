

public class Function {

    private float val;

    Function(){}

    public synchronized float get(){
        return val;
    }

    public synchronized void set(float res){
        this.val = res;
    }
}

