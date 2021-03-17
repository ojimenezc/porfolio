package extending_thread_class;
public class DemoThread extends Thread{

    public void run(){
        try{
            System.out.println(String.format("Running thread %s", Thread.currentThread().getId()));
        }catch(Exception exception){
            System.out.println(String.format("Error running thread %s", exception.getMessage()));
        }
    }
}
