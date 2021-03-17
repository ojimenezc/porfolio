package implementing_runnable;

public class DemoThreadRunnable implements Runnable {
    @Override
    public void run(){
        try{
            System.out.println(String.format("Running thread %s", Thread.currentThread().getName()));
        }catch(Exception exception){
            System.out.println(String.format("Error running thread %s", exception.getMessage()));
        }
    }
}
