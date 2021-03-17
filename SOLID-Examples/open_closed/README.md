# Open Closed
The open closed principle aims at being able to extend the class funcionality without touching the base existing stable code.
This folder contains a correct implementation of this principle.

The implementation contains an interface that defines the signature for the printing service classes, then 2 different printing services
one for console and other for device printing, and a printing service manager which uses the interface as argument to receive any class that 
implements the InvoicePrinting interface.
