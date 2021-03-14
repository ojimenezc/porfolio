import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {TaxesService} from '../services/taxes.service';
import {UnitsService} from '../services/units.service';
import {CurrenciesService} from '../services/currencies.service';
import {LoadingController, NavController} from '@ionic/angular';
import {ProductsService} from '../services/products.service';
import {StorageService} from '../services/storage.service';
import {RatesService} from '../services/rates.service';

@Component({
    selector: 'app-edit-prod',
    templateUrl: './edit-prod.page.html',
    styleUrls: ['./edit-prod.page.scss'],
})
export class EditProdPage implements OnInit {


    public taxesList;
    public selectedTax = {name: '', code: ''};
    public selectedRateType = {name: '', code: ''};
    public rateTypes;
    public selectedUnit = {name: '', code: ''};
    public unitMeasure;
    public currencies;
    public selectedCurrency = {name: '', code: ''};
    public loading: any;

    public newProductForm;

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
        await this.loadProdToEdit();
        await this.dismissLoading();
    }

    async loadFormValues() {
        this.loadTaxes();
        this.loadRateTypes();
        this.loadUnits();
        this.loadCurrencies();
    }

    private async loadProdToEdit() {
        const edit = await this.storage.get('edit_prod');
        const p = await this.productsService.getsingle(edit.id);
        const prod = JSON.parse(JSON.stringify(p));
        if (prod !== undefined) {
            this.newProductForm = this.formBuilder.group({
                taxType: new FormControl(prod.taxType, Validators.required),
                rateType: new FormControl(prod.taxRateCode, Validators.required),
                unitMeasure: new FormControl(prod.unitMeasure, Validators.required),
                productName: new FormControl(prod.productName, Validators.required),
                productPrice: new FormControl(prod.productPrice, Validators.required),
                prodCurrency: new FormControl(prod.prodCurrency, Validators.required),
                id: new FormControl(prod.id, Validators.required),
                customerId: new FormControl(prod.customerId, Validators.required),
                productCode: new FormControl(prod.productCode, Validators.required)
            });

            await this.loadFormValues();
        }
    }

    private async loadTaxes() {
        try {
            this.taxesList = await this.taxesService.get();
            this.selectedTax = this.taxesList.find(t => {
                return t.code === this.newProductForm.value.taxType;
            });
        } catch (e) {
            console.log(e);
        }
    }

    private async loadCurrencies() {
        try {
            this.currencies = await this.currencyService.get();
            this.selectedCurrency = this.currencies.find(t => {
                return t.code === this.newProductForm.value.prodCurrency;
            });
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
        this.selectedUnit = this.unitMeasure.find(t => {
            return t.code === this.newProductForm.value.unitMeasure;
        });
    }

    private async loadRateTypes() {
        this.rateTypes = await this.ratesService.get();
        this.selectedRateType = this.rateTypes.find(r => {
            return r.code === this.newProductForm.value.rateType;
        });
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
            await this.productsService.save(prod);
            this.navigation.navigateForward('products');
            this.dismissLoading();

        } catch (e) {
            this.dismissLoading();
            console.log(JSON.stringify(e));
        }
    }
}
