package single_responsibility;
/**
 * This class is for demonstration purposes. No getters are created for simplicity.
 * 
 * Why is this class right?
 * This class follows the Single responsiblity principle because the class contains only the logic related
 * to the invoice.
 */
public class Invoice {
    private double totalAmount;
    private double totalTaxes;
    private double totalBeforeTaxes;
    private double discount;
    private int clientId;
    private Object[] products;


    public double getTotalAmount(){
        return this.totalAmount;
    }

    public double getTotalTaxes(){
        return this.totalTaxes;
    }

    public double getTotalBeforeTaxes(){
        return this.totalBeforeTaxes;
    }

    public double getDiscount() {
        return this.discount;
    }

    public int getClientId() {
        return this.clientId;
    }

    public Object[] getProducts(){
        return this.products;
    }

    public Invoice(double totalBeforeTaxes,double totalTaxes,double discount,int clientId,Object[] products){
        this.totalBeforeTaxes = totalBeforeTaxes;
        this.totalTaxes = totalTaxes;
        this.discount = discount;
        this.clientId=clientId;
        this.products = products;
        this.totalAmount = (totalBeforeTaxes-discount) + totalTaxes;
    }
}
