package open_closed.printing;

public class DevicePrinting implements InvoicePrinting{
    private Invoice invoice;

    public DevicePrinting(Invoice invoice){
        this.invoice=invoice;
    }

    @Override
    public void printInvoice(){
        //Add some logic to print to a device here
    }
}
