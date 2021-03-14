import {Component, OnInit} from '@angular/core';
import {CompanyService} from '../services/company.service';
import {IdentificationTypesService} from '../services/identification-types.service';
import {ProvincesService} from '../services/provinces.service';
import {CountiesService} from '../services/counties.service';
import {DistrictsService} from '../services/districts.service';
import {LoadingController} from '@ionic/angular';
import {CurrenciesService} from '../services/currencies.service';
import {StorageService} from '../services/storage.service';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {Chooser} from '@ionic-native/chooser/ngx';

let base64String;

@Component({
    selector: 'app-add-company',
    templateUrl: './add-company.page.html',
    styleUrls: ['./add-company.page.scss'],
})

export class AddCompanyPage implements OnInit {
    public companyForm: any;

    public companies;
    public identificationTypes: any;
    public counties: any;
    public districts: any;
    public provinces: any;
    public loading: any;
    public currencies: any;
    public certificateFileName = '';
    public base64Cert = '';

    constructor(private companyService: CompanyService,
                private identificationTypesService: IdentificationTypesService,
                private provincesService: ProvincesService,
                private countiesService: CountiesService,
                private districtsService: DistrictsService,
                private loadingController: LoadingController,
                private currenciesService: CurrenciesService,
                private storage: StorageService,
                private chooser: Chooser,
                private router: Router,
                private formBuilder: FormBuilder) {

    }

    async ngOnInit() {
        this.createForm();
        await this.presentLoading();
        this.loadIdentificationTypes().then();
        this.loadProvinces().then();
        this.loadCounties().then();
        this.loadDistricts().then();
        this.loadCurrencies().then();
        this.dismissLoading();
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

    /* changeListener($event): void {
         const file = $event.target.files[0];
         const reader = new FileReader();

         // tslint:disable-next-line:only-arrow-functions
         reader.onloadend = function () {
             const f = reader.result.toString().replace(/^data:(.*,)?/, '');
             base64String = f;
             console.log(base64String);
         }

         if (file) {
             reader.readAsDataURL(file);
         } else {
             console.log('Not file read');
         }
     }*/

    changeListener($event): void {
        this.readThis($event.target);
    }

    readThis(inputValue: any): void {
        const file: File = inputValue.files[0];
        const myReader: FileReader = new FileReader();

        myReader.onloadend = (e) => {
            this.base64Cert = myReader.result.toString().replace(/^data:(.*,)?/, '');
        }
        myReader.readAsDataURL(file);
    }


    async loadCurrencies() {
        this.currencies = await this.currenciesService.get();
    }

    async loadIdentificationTypes() {
        this.identificationTypes = await this.identificationTypesService.get();
    }

    private createForm() {

        this.companyForm = this.formBuilder.group({
            id: new FormControl('0'),
            companyName: new FormControl('', Validators.required),
            businessName: new FormControl('', Validators.required),
            identificationType: new FormControl('', Validators.required),
            identificationNumber: new FormControl('', Validators.required),
            contactEmail: new FormControl('', Validators.compose([
                Validators.required,
                Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')
            ])),
            contactPhone: new FormControl('', Validators.required),
            countryId: new FormControl('1', Validators.required),
            provinceId: new FormControl('', Validators.required),
            countyId: new FormControl('', Validators.required),
            districtId: new FormControl('', Validators.required),
            neigborhoodId: new FormControl('1', Validators.required),
            postalCode: new FormControl('', Validators.required),
            defaultCurrency: new FormControl('', Validators.required),
            atvUsername: new FormControl('', Validators.required),
            atvPassword: new FormControl('', Validators.required),
            certificatePin: new FormControl('', Validators.required),
            atvCertificate: new FormControl('', Validators.required)
        });
    }

    async loadProvinces() {
        this.provinces = await this.provincesService.get();
    }

    async loadCounties() {

        if (this.companyForm.controls.provinceId !== undefined && this.companyForm.controls.provinceId.value.length > 0) {
            this.counties = await this.countiesService.get(this.companyForm.controls.provinceId.value);
        } else {
            this.counties = await this.countiesService.get(1);
        }
    }

    async loadDistricts() {

        if (this.companyForm.controls.provinceId !== undefined && this.companyForm.controls.provinceId.value.length > 0 &&
            this.companyForm.controls.countyId !== undefined && this.companyForm.controls.countyId.value.length > 0) {
            this.districts = await this.districtsService.get(this.companyForm.controls.countyId.value, this.companyForm.controls.provinceId.value);
        } else {
            this.districts = await this.districtsService.get(1, 1);
        }
    }

    public async save() {
        try {
            await this.presentLoading();
            const userInfo = await this.storage.getUserInfo();
            const request = {
                customerId: userInfo.id,
                company: this.companyForm.value
            };
            const result = await this.companyService.save(request);
            this.dismissLoading();
            this.router.navigateByUrl('company');
        } catch (e) {
            console.error(e);
            this.dismissLoading();
        }
    }

    public async chooseCert() {
        this.chooser.getFile().then(file => {
            const certificateBase64 = btoa(String.fromCharCode.apply(null, file.data));
            this.base64Cert = certificateBase64;
            this.certificateFileName = file.name;
        }).catch(file => {
            const certificateBase64 = btoa(String.fromCharCode.apply(null, file.data));
            this.base64Cert = certificateBase64;
            this.certificateFileName = file.name;
        });
    }
}
