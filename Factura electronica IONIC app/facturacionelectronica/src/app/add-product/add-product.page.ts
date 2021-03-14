import {Component, OnInit} from '@angular/core';
import {TaxesService} from '../services/taxes.service';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {UnitsService} from '../services/units.service';
import {LoadingController, NavController} from "@ionic/angular";
import {CurrenciesService} from "../services/currencies.service";
import {ProductsService} from "../services/products.service";
import {StorageService} from "../services/storage.service";
import {RatesService} from "../services/rates.service";

@Component({
    selector: 'app-add-product',
    templateUrl: './add-product.page.html',
    styleUrls: ['./add-product.page.scss'],
})
export class AddProductPage implements OnInit {
    public taxesList;
    public selectedTax = {name: '', code: ''};
    public selectedRateType = {name: '', code: ''};
    public rateTypes;
    public selectedUnit = {name: '', code: ''};
    public unitMeasure;
    public currencies;
    public selectedCurrency = {name: '', code: ''};
    public loading: any;
    public newProductForm = this.formBuilder.group({
        taxType: new FormControl('', Validators.required),
        rateType: new FormControl('', Validators.required),
        unitMeasure: new FormControl('', Validators.required),
        productName: new FormControl('', Validators.required),
        productCode: new FormControl('', Validators.required),
        productPrice: new FormControl('', Validators.required),
        prodCurrency: new FormControl('', Validators.required)
    });

    constructor(private taxesService: TaxesService,
                private units: UnitsService,
                private currencyService: CurrenciesService,
                private loadingController: LoadingController,
                private productsService: ProductsService,
                private storage: StorageService,
                private ratesService: RatesService,
                private navigation: NavController,
                private formBuilder: FormBuilder) {
    }

    async ngOnInit() {
        await this.presentLoading();
        this.loadTaxes();
        this.loadRateTypes();
        this.loadUnits().then();
        this.loadCurrencies().then();
        await this.dismissLoading();
    }

    private async loadTaxes() {
        try {
            this.taxesList = await this.taxesService.get();
            this.selectedTax = this.taxesList[0];
        } catch (e) {
            console.log(e);
        }
    }

    private async loadCurrencies() {
        try {
            this.currencies = await this.currencyService.get();
            this.selectedCurrency = this.currencies[0];
        } catch (e) {
            console.log(e);
        }
    }

    public changeCurrency(event) {
        this.selectedCurrency = this.currencies.find(t => {
            return t.code === event.detail.value;
        });
    }

    public changeTax(event) {
        this.selectedTax = this.taxesList.find(t => {
            return t.code === event.detail.value;
        });
    }

    public changeRateType(event) {
        this.selectedRateType = this.rateTypes.find(t => {
            return t.code === event.detail.value;
        });
    }

    public changeUnitMeasure(event) {
        this.selectedUnit = this.unitMeasure.find(t => {
            return t.code === event.detail.value;
        });
    }

    private async loadUnits() {
        this.unitMeasure = await this.units.get();
        this.selectedUnit = this.unitMeasure[0];
    }

    private async loadRateTypes() {
        this.rateTypes = await this.ratesService.get();
        this.selectedRateType = this.rateTypes[0];
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

    public async save() {
        try {
            this.presentLoading();
            const prod = this.newProductForm.value;
            const userInfo = await this.storage.getUserInfo();
            prod.customerId = userInfo.id;
            await this.productsService.save(prod);
            this.navigation.navigateForward('products');
            this.dismissLoading();
        } catch (e) {
            this.dismissLoading();
            console.log(JSON.stringify(e));
        }
    }
}
