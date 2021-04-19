import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Functional {

    public static boolean Exec(String s) throws IOException {
        System.out.println("Function " + s + " has not finished calculating. Press 'c' to continue or 'i' to interrupt "+s);
        char ch;
        int code;
        while (-1 != (code = System.in.read())) {

            if ((char)code == 'c')
                return true;

            else if((char)code == 'i')
                return false;
        }
        return false;
    }

    static float getX(){
        float x = 0;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter x: ");
        try{
            x = in.nextFloat();
        }
        catch (InputMismatchException e){
            System.out.println("Error input");
            System.exit(0);
        }
        return x;
    }

}
