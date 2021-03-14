import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {CompanyService} from '../services/company.service';
import {AlertController, LoadingController, NavController} from '@ionic/angular';
import {ClientsService} from '../services/clients.service';
import {StorageService} from '../services/storage.service';
import {ProductsService} from '../services/products.service';
import {ModalController} from '@ionic/angular';
import {AddProductModalPage} from '../add-product-modal/add-product-modal.page';
import {TranslateService} from '@ngx-translate/core';
import {DocumentTypesService} from '../services/document-types.service';
import {DocumentGeneratorService} from '../services/document-generator.service';
import {ExchangeRateService} from '../services/exchange-rate.service';
import {CurrenciesService} from '../services/currencies.service';
import {DocumentsService} from "../services/documents.service";

@Component({
    selector: 'app-invoicing',
    templateUrl: './invoicing.page.html',
    styleUrls: ['./invoicing.page.scss'],
})
export class InvoicingPage implements OnInit {
    public invoiceForm = this.formGroupBuilder.group({});
    public selectedCompany: any;
    public companies;
    public loading;
    public selectedOffice;
    public offices;
    public selectedPos;
    public pointOfSales;
    public clients;
    public selectedClient;
    public products;
    public selectedProduct;
    public selectedSellCondition;
    public sellConditions;
    public invoiceDetails = [];
    public documentTypes = [];
    public selectedDocumenType;
    public dueDays = 0;
    public paymentMethods = [];
    public selectedPaymentMethod;
    private invoice = {};
    public exchangeRate;
    public currencies: any;
    public selectedCurrency;

    constructor(private formGroupBuilder: FormBuilder,
                private companyService: CompanyService,
                private storageService: StorageService,
                private clientsService: ClientsService,
                private alertController: AlertController,
                private productsService: ProductsService,
                private translate: TranslateService,
                private currenciesService: CurrenciesService,
                private exchangeRateService: ExchangeRateService,
                private documentTypesService: DocumentTypesService,
                private modalController: ModalController,
                private documentGeneratorService: DocumentGeneratorService,
                private documentsService: DocumentsService,
                private navController: NavController,
                private loadingController: LoadingController) {
    }

    async ngOnInit() {
        await this.presentLoading();
        await this.loadClients();
        await this.loadCompanies();
        await this.loadOffices();
        await this.loadProducts();
        await this.loadSellConditions();
        this.loadDocumentTypes();
        this.loadPaymentMethods();
        this.loadExchangeRate();
        this.loadCurrencies();
        await this.dismissLoading();
    }


    async loadCurrencies() {
        this.currencies = await this.currenciesService.get();
        this.selectedCurrency = this.currencies[0];
    }

    async loadCompanies() {
        this.companies = await this.companyService.getAll();
        this.selectedCompany = this.companies[0];
    }

    public changeCurrency(event) {
        this.selectedCurrency = this.currencies.find(i => {
            return i.id === event.detail.value;
        });
    }

    async loadClients() {
        const userInfo = await this.storageService.getUserInfo();
        this.clients = await this.clientsService.get(userInfo.id);
        this.selectedClient = this.clients[0];
    }

    async loadSellConditions() {
        this.sellConditions = [];
        this.sellConditions.push({
            id: '01',
            name: 'Contado'
        });
        this.sellConditions.push({
            id: '02',
            name: 'Crédito'
        });
        this.sellConditions.push({
            id: '03',
            name: 'Consignación'
        });
        this.sellConditions.push({
            id: '04',
            name: 'Apartado'
        });
        this.sellConditions.push({
            id: '05',
            name: 'Arrendamiento con opción de compra'
        });
        this.sellConditions.push({
            id: '06',
            name: 'Arrendamiento en función financiera'
        });
        this.sellConditions.push({
            id: '07',
            name: 'Cobro a favor de un tercero'
        });
        this.sellConditions.push({
            id: '08',
            name: 'Servicios prestados al estado a credito'
        });
        this.sellConditions.push({
            id: '09',
            name: 'pago del servicio prestado al estado'
        });
        this.sellConditions.push({
            id: '99',
            name: 'Otros'
        });

        this.selectedSellCondition = this.sellConditions[0];
    }

    async changeSellCondition(event) {
        this.selectedSellCondition = this.sellConditions.find(i => {
            return i.id === event.detail.value;
        });
    }

    async loadProducts() {
        const userInfo = await this.storageService.getUserInfo();
        this.products = await this.productsService.get(userInfo.id);
        this.selectedProduct = this.products[0];
    }

    async changeSelectedProduct(event) {
        this.selectedProduct = this.products.find(i => {
            return i.id === event.detail.value;
        });
    }

    public generate() {

    }

    async presentLoading() {
        this.loading = await this.loadingController.create({
            message: ''
        });
        await this.loading.present();
    }

    async dismissLoading() {
        await this.loading.dismiss();
    }

    async changeSelectedClient(event) {
        if (this.clients !== undefined) {
            this.selectedClient = this.clients.find(i => {
                return i.id === event.detail.value;
            });
        }
    }


    public async changeCompany(event) {
        if (this.companies !== undefined) {
            this.selectedCompany = this.companies.find(c => {
                return c.id === event.detail.value;
            });
        }
    }

    public async changeOffice(event) {
        if (this.offices !== undefined) {
            this.selectedOffice = this.offices.find(c => {
                return c.office.id === event.detail.value;
            });
        }
    }

    public async loadOffices() {
        if (this.selectedCompany !== undefined) {
            this.offices = await this.companyService.getOffices(this.selectedCompany.id);
            this.selectedOffice = this.offices[0];
            this.pointOfSales = this.selectedOffice.pointsOfSale;
            this.selectedPos = this.pointOfSales[0];
        }
    }


    public changeSelectedPos(event) {
        if (this.pointOfSales !== undefined) {
            this.selectedPos = this.pointOfSales.find(i => {
                return i.id === event.detail.value;
            });
        }
    }

    public async addProduct() {
        const modal = await this.modalController.create({
            component: AddProductModalPage,
            componentProps: {
                products: this.products
            }
        });
        modal.onDidDismiss().then((detail) => {
            if (detail !== null) {
                this.validateAddedProduct(detail.data);
            }
        });
        return modal.present();
    }

    private validateAddedProduct(detail) {
        const existing = this.invoiceDetails.find(d => {
            return d.product.id === detail.product.id;
        });

        if (existing === undefined) {
            this.invoiceDetails.push(detail);
        } else {
            this.invoiceDetails.splice(this.invoiceDetails.indexOf(existing), 1);

            if (!detail.editing) {
                existing.quantity += detail.quantity;
            } else {
                existing.quantity = detail.quantity;
            }

            this.invoiceDetails.push(existing);
        }
    }

    public async editItem(item) {
        const modal = await this.modalController.create({
            component: AddProductModalPage,
            componentProps: {
                editItem: item,
                products: this.products
            }
        });
        modal.onDidDismiss().then((detail) => {
            if (detail !== null) {
                this.validateAddedProduct(detail.data);
            }
        });
        return modal.present();
    }

    public async removeItem(item) {
        const alertMessage = await this.translate.get('DELETE_ITEM').toPromise();
        const confirmBtn = await this.translate.get('DELETE_CONFIRM').toPromise();
        const cancelBtn = await this.translate.get('DELETE_CANCEL').toPromise();

        const alert = await this.alertController.create({
            header: '',
            message: alertMessage,
            buttons: [
                {
                    text: cancelBtn,
                    role: 'cancel',
                    cssClass: 'secondary',
                    handler: () => {
                    }
                }, {
                    text: confirmBtn,
                    handler: () => {
                        this.invoiceDetails.splice(this.invoiceDetails.indexOf(item), 1);
                    }
                }
            ]
        });

        await alert.present();
    }

    async loadDocumentTypes() {

        this.documentTypes.push({
            name: 'Factura Electronica',
            code: '01'
        });
        this.documentTypes.push({
            name: 'Tiquete Electronico',
            code: '04'
        });
        this.documentTypes.push({
            name: 'Factura electronica de compra',
            code: '08'
        });
        /* this.documentTypes.push({
             name: 'Factura electronica de exportacion',
             code: '09'
         });*/

        this.selectedDocumenType = this.documentTypes[0];
    }

    public changeDocumentType(event) {
        if (this.documentTypes !== undefined) {
            this.selectedDocumenType = this.documentTypes.find(i => {
                return i.code === event.detail.value;
            });
        }
    }

    private async loadPaymentMethods() {
        this.paymentMethods.push({
            name: 'Efectivo',
            code: '01'
        });
        this.paymentMethods.push({
            name: 'Tarjeta',
            code: '02'
        });
        this.paymentMethods.push({
            name: 'Cheque',
            code: '03'
        });
        this.paymentMethods.push({
            name: 'Transferencia - depósito bancario',
            code: '04'
        });
        this.paymentMethods.push({
            name: 'Recaudado por terceros',
            code: '05'
        });

        this.paymentMethods.push({
            name: 'Otros',
            code: '99'
        });

        this.selectedPaymentMethod = this.paymentMethods[0];
    }

    public changePaymentMethod(event) {
        if (this.paymentMethods !== undefined) {
            this.selectedPaymentMethod = this.paymentMethods.find(i => {
                return i.code === event.detail.value;
            });
        }
    }

    public async createInvoice() {
        this.presentLoading().then();
        const paymentMethods = [];
        paymentMethods.push(this.selectedPaymentMethod.code);

        this.invoice = await this.documentGeneratorService.get(this.selectedCompany,
            this.selectedPos, this.selectedOffice.office, this.selectedDocumenType, this.selectedClient,
            this.selectedSellCondition, this.dueDays, paymentMethods, this.invoiceDetails, this.selectedCurrency, this.exchangeRate);

        await this.documentsService.save(this.invoice);

        this.dismissLoading().then();
        this.navController.navigateForward('documents').then();
    }

    private async loadExchangeRate() {
        this.exchangeRate = await this.exchangeRateService.get();

    }
}

