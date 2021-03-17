public class HourlyFeeService implements PaidService,Service {

    @Override
    public void execute(){
        //DO something
    }
    
    @Override
    public double calculateFee(){
        //Do something
        return 10;
    }
}
