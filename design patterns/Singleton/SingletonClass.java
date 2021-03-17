public class SingletonClass{

    private static SingletonClass instance;
    /**
    *Creating the constructor as private is required to avoid the direct creation
    *of instances
    */
    private SingletonClass(){}

    public static SingletonClass getInstance(){
        if(null==this.instance){
            this.instance = new SingletonClass();
        }
        return this.instance;
    }
}