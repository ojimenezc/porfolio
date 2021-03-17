package correct_implementation;

/**
 * This class is right since its only responsibility is printing invoices, nothing else.
 */
public class InvoicePrinting {
    
    private Invoice invoice;

    public InvoicePrinting(Invoice invoice){
        this.invoice=invoice;
    }

    public void printInvoice(){
        System.out.println(String.format("%s = %d", "CLIENT ID", this.invoice.getClientId()));
        System.out.println(String.format("%s = %d", "SUBTOTAL", this.invoice.getTotalBeforeTaxes()));
        System.out.println(String.format("%s = %d", "TAXES", this.invoice.getTotalTaxes()));
        System.out.println(String.format("%s = %d", "DISCOUNT", this.invoice.getDiscount()));
        System.out.println(String.format("%s = %d", "TOTAL", this.invoice.getTotalAmount()));
    }
}
