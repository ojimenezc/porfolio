package implementing_runnable;
public class main {
    
    public static void main(String[] args){

        for(int i=0;i<=10;i++){
            Thread thread = new Thread(new DemoThreadRunnable(),String.valueOf(i));
            thread.start();
        }
    }
}
