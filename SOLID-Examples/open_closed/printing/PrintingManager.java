package open_closed.printing;

import invoices.Invoice;
public class PrintingManager {

    InvoicePrinting invoicePrintingService;

    public PrintingManager(InvoicePrinting printingService){
        this.invoicePrintingService = printingService;
    }

    public void Print(Invoice invoice){
        this.invoicePrintingService.printInvoice(invoice);
    }
}
