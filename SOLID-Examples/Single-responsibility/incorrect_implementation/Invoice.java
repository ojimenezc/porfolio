package incorrect_implementation;
/**
 * This class is for demonstration purposes. No getters are created for simplicity.
 * 
 * Why is this class wrong?
 * This class does not follow the Single responsiblity principle because the class contains the 
 * printing invoice logic, so if we need to change only the printing logic we will need to modify 
 * the invoice class which does not make sense.
 */
public class Invoice {
    private double totalAmount;
    private double totalTaxes;
    private double totalBeforeTaxes;
    private double discount;
    private int clientId;
    private Object[] products;

    public Invoice(double totalBeforeTaxes,double totalTaxes,double discount,int clientId,Object[] products){
        this.totalBeforeTaxes = totalBeforeTaxes;
        this.totalTaxes = totalTaxes;
        this.discount = discount;
        this.clientId=clientId;
        this.products = products;
        this.totalAmount = (totalBeforeTaxes-discount) + totalTaxes;
    }

    public void printInvoice(){
        System.out.println(String.format("%s = %d", "CLIENT ID", this.clientId));
        System.out.println(String.format("%s = %d", "SUBTOTAL", this.totalBeforeTaxes));
        System.out.println(String.format("%s = %d", "TAXES", this.totalTaxes));
        System.out.println(String.format("%s = %d", "DISCOUNT", this.discount));
        System.out.println(String.format("%s = %d", "TOTAL", this.totalAmount));
    }
}
