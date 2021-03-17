package extending_thread_class;

public class main {
    
    public static void main(String[] args){

        for(int i=0;i<=10;i++){
            DemoThread thread = new DemoThread();
            thread.start();
        }
    }
}
