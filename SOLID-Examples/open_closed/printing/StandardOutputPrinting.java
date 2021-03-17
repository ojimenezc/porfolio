package open_closed.printing;

public class StandardOutputPrinting {
    public class StandardOutputPrinting implements InvoicePrinting {
        private Invoice invoice;
    
        public ConsolePrinting(Invoice invoice){
            this.invoice=invoice;
        }
    
        @Override
        public void printInvoice(){
            System.out.println(String.format("%s = %d", "CLIENT ID", this.invoice.getClientId()));
            System.out.println(String.format("%s = %d", "SUBTOTAL", this.invoice.getTotalBeforeTaxes()));
            System.out.println(String.format("%s = %d", "TAXES", this.invoice.getTotalTaxes()));
            System.out.println(String.format("%s = %d", "DISCOUNT", this.invoice.getDiscount()));
            System.out.println(String.format("%s = %d", "TOTAL", this.invoice.getTotalAmount()));
        }
    }
}
