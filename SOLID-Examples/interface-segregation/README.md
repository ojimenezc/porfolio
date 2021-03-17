# Interface segregation

The principle states that many specific interfaces are better than a single one.
In this example we have a service class and a free service class. The service class needs to calculate the fee to charge and the free service should not
implement that logic so here the interfaces are segregated one for general services, other for charged services and other for free services.