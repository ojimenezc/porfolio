import {Component, OnInit} from '@angular/core';
import {CompanyService} from '../services/company.service';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {IdentificationTypesService} from '../services/identification-types.service';
import {ProvincesService} from '../services/provinces.service';
import {CountiesService} from '../services/counties.service';
import {DistrictsService} from '../services/districts.service';
import {AlertController, LoadingController} from '@ionic/angular';
import {CurrenciesService} from '../services/currencies.service';
import {StorageService} from '../services/storage.service';
import {Chooser} from '@ionic-native/chooser/ngx';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-company',
    templateUrl: './company.page.html',
    styleUrls: ['./company.page.scss'],
})
export class CompanyPage implements OnInit {
    public companyForm: any;
    public consecutivesForm: any;
    public companies: any;
    public selectedCompany: any = {};
    public identificationTypes: any;
    public selectedIdentificationTypeName: any;
    public selectedProvinceName: any;
    public selectedCountyName: any;
    public selectedDistrictName: any;
    public selectedCurrencyName: any;
    public counties: any;
    public districts: any;
    public provinces: any;
    public loading: any;
    public currencies: any;
    public creating = false;
    public certificateFileName = '';
    public base64Cert = '';
    public selectedSegment = 1;
    public electronicBillsConsecutive: any;
    public debitNotesConsecutive: any;
    public creditNotesBillsConsecutive: any;
    public electronicTicketConsecutive: any;
    public acceptanceConfirmationConsecutive: any;
    public rejectionConsecutive: any;
    public partialAcceptanceConsecutive: any;
    public buyingElectronicBillConsecutive: any;
    public exportElectronicBillingConsecutive: any;
    public updatedConsecutives: [];
    public offices;
    public selectedOffice;
    public selectedPos;

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
                private translate: TranslateService,
                private alertController: AlertController,
                private formBuilder: FormBuilder) {
    }

    ngOnInit() {
    }

    async ionViewWillEnter() {
        this.load();
    }

    async load() {
        await this.presentLoading();
        await this.loadCompanies();

        if (this.companies != null && this.companies.length > 0) {
            this.selectedCompany = this.companies[0];
            this.loadIdentificationTypes().then(() => {
                this.loadCompanySelectedValues();
            });
            this.loadProvinces().then(() => {
                this.loadCompanySelectedValues();
            });
            this.loadCounties().then(() => {
                this.loadCompanySelectedValues();
            });
            this.loadDistricts().then(() => {
                this.loadCompanySelectedValues();
            });
            this.loadCurrencies().then(() => {
                this.loadCompanySelectedValues();
            });
            this.loadSelectedCompany();
        }

        if (this.selectedCompany != null) {
            this.loadOffices().then();
        }

        this.dismissLoading();
        this.loadConsecutivesFormGroup().then();
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

    async loadCurrencies() {
        this.currencies = await this.currenciesService.get();
    }

    async loadIdentificationTypes() {
        this.identificationTypes = await this.identificationTypesService.get();
    }

    private loadCompanySelectedValues() {
        if (this.identificationTypes !== undefined) {
            const selectedIdentificationType = this.identificationTypes.find(i => {
                return i.id === this.companyForm.value.identificationType;
            });

            if (selectedIdentificationType != null || selectedIdentificationType !== undefined) {
                this.selectedIdentificationTypeName = selectedIdentificationType.name;
            }
        }

        if (this.provinces !== undefined) {
            const selectedProvince = this.provinces.find(i => {
                return i.code === this.companyForm.value.provinceId;
            });
            if (selectedProvince != null) {
                this.selectedProvinceName = selectedProvince.name;
            }
        }

        if (this.counties !== undefined) {
            const selectedCounty = this.counties.find(i => {
                return i.code === this.companyForm.value.countyId;
            });
            if (selectedCounty != null) {
                this.selectedCountyName = selectedCounty.name;
            }
        }

        if (this.districts !== undefined) {
            const selectedDistrict = this.districts.find(i => {
                return i.code === this.companyForm.value.districtId;
            });
            if (selectedDistrict != null) {
                this.selectedDistrictName = selectedDistrict.name;
            }
        }

        if (this.currencies !== undefined) {
            const selectedCurrency = this.currencies.find(i => {
                return i.id === this.companyForm.value.defaultCurrency;
            });
            if (selectedCurrency != null) {
                this.selectedCurrencyName = selectedCurrency.code;
            }
        }
    }

    private async loadCompanies() {
        this.companies = await this.companyService.getAll();
        this.loadSelectedCompany();
    }

    private loadSelectedCompany() {
        this.companyForm = this.formBuilder.group({
            id: new FormControl(this.selectedCompany.id, Validators.required),
            companyName: new FormControl(this.selectedCompany.companyName, Validators.required),
            businessName: new FormControl(this.selectedCompany.businessName, Validators.required),
            identificationType: new FormControl(this.selectedCompany.identificationType, Validators.required),
            identificationNumber: new FormControl(this.selectedCompany.identificationNumber, Validators.required),
            contactEmail: new FormControl(this.selectedCompany.contactEmail, Validators.compose([
                Validators.required,
                Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')
            ])),
            contactPhone: new FormControl(this.selectedCompany.contactPhone, Validators.required),
            countryId: new FormControl('1', Validators.required),
            provinceId: new FormControl(this.selectedCompany.provinceId, Validators.required),
            countyId: new FormControl(this.selectedCompany.countyId, Validators.required),
            districtId: new FormControl(this.selectedCompany.districtId, Validators.required),
            neigborhoodId: new FormControl('1', Validators.required),
            postalCode: new FormControl(this.selectedCompany.postalCode, Validators.required),
            defaultCurrency: new FormControl(this.selectedCompany.defaultCurrency, Validators.required),
            atvUsername: new FormControl(this.selectedCompany.atvUsername, Validators.required),
            atvPassword: new FormControl(this.selectedCompany.atvPassword, Validators.required),
            certificatePin: new FormControl(this.selectedCompany.certificatePin, Validators.required),
            atvCertificate: new FormControl(this.selectedCompany.atvCertificate),
            active: new FormControl(this.selectedCompany.active, Validators.required)
        });
    }

    private async loadConsecutivesFormGroup() {
        this.consecutivesForm = this.formBuilder.group({
            electronicBillsConsecutive: new FormControl('', Validators.required),
            debitNotesConsecutive: new FormControl('', Validators.required),
            creditNotesBillsConsecutive: new FormControl('', Validators.required),
            electronicTicketConsecutive: new FormControl('', Validators.required),
            acceptanceConfirmationConsecutive: new FormControl('', Validators.required),
            partialAcceptanceConsecutive: new FormControl('', Validators.required),
            rejectionConsecutive: new FormControl('', Validators.required),
            buyingElectronicBillConsecutive: new FormControl('', Validators.required),
            exportElectronicBillingConsecutive: new FormControl('', Validators.required)
        });
    }

    async loadProvinces() {
        this.provinces = await this.provincesService.get();
    }

    async loadCounties() {
        if (this.companyForm.value.provinceId !== null) {

            this.counties = await this.countiesService.get(this.companyForm.value.provinceId);

        } else if (this.selectedCompany !== null && this.selectedCompany.provinceId !== undefined) {
            this.counties = await this.countiesService.get(this.selectedCompany.provinceId);
        } else {
            this.counties = await this.countiesService.get(1);
        }
    }

    async loadDistricts() {
        if (this.companyForm.value.countyId !== null && this.companyForm.value.provinceId !== null) {

            this.districts = await this.districtsService.get(this.companyForm.value.countyId, this.companyForm.value.provinceId);

        } else if (this.selectedCompany != null && this.selectedCompany.countyId !== undefined) {
            this.districts = await this.districtsService.get(this.selectedCompany.countyId, this.selectedCompany.provinceId);
        } else {
            this.districts = await this.districtsService.get(1, 1);
        }
    }

    public async save() {
        await this.presentLoading();
        const userInfo = await this.storage.getUserInfo();
        const request = {
            customerId: userInfo.id,
            company: this.companyForm.value
        };
        this.selectedCompany = request.company;
        const result = await this.companyService.save(request);
        await this.loadCompanies();
        this.dismissLoading();
    }

    public changeCompany(event) {
        this.selectedCompany = this.companies.find(i => {
            return i.id === event.detail.value;
        });
        this.loadSelectedCompany();
        this.loadCompanySelectedValues();
        this.loadOffices().then();
    }

    public new() {
        this.router.navigateByUrl('add-company');
    }

    public cancel() {
        this.creating = false;
        this.loadSelectedCompany();
        this.loadCompanySelectedValues();
    }

    changeListener($event): void {
        this.readThis($event.target);
    }

    readThis(inputValue: any): void {
        const file: File = inputValue.files[0];
        const myReader: FileReader = new FileReader();

        myReader.onloadend = (e) => {
            this.base64Cert = myReader.result.toString().replace(/^data:(.*,)?/, '');
            console.log('Base64 File', this.base64Cert);
        }
        myReader.readAsDataURL(file);
    }

    public async delete() {

        const alertMessage = await this.translate.get('COMPANY_DELETE').toPromise();
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
                    handler: async () => {
                        this.presentLoading();
                        await this.companyService.delete(this.selectedCompany.id);
                        this.loadCompanies();
                        this.dismissLoading();
                    }
                }
            ]
        });

        await alert.present();
    }

    public async loadConsecutives() {
        if (this.selectedOffice !== undefined) {

            const consecutives = JSON.parse(JSON.stringify(await this.companyService.getConsecutives(this.selectedCompany.id,
                this.selectedOffice.office.id, this.selectedPos.id)));

            if (consecutives !== undefined) {

                this.electronicBillsConsecutive = consecutives.find(c => {
                    return c.documentType === 1;
                });
                if (this.electronicTicketConsecutive === undefined) {
                    this.electronicBillsConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 1,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }

                this.debitNotesConsecutive = consecutives.find(c => {
                    return c.documentType === 2;
                });
                if (this.debitNotesConsecutive === undefined) {
                    this.debitNotesConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 2,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }

                this.creditNotesBillsConsecutive = consecutives.find(c => {
                    return c.documentType === 3;
                });
                if (this.creditNotesBillsConsecutive === undefined) {
                    this.creditNotesBillsConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 3,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }


                this.electronicTicketConsecutive = consecutives.find(c => {
                    return c.documentType === 4;
                });
                if (this.electronicTicketConsecutive === undefined) {
                    this.electronicTicketConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 4,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }

                this.acceptanceConfirmationConsecutive = consecutives.find(c => {
                    return c.documentType === 5;
                });
                if (this.acceptanceConfirmationConsecutive === undefined) {
                    this.acceptanceConfirmationConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 5,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }

                this.partialAcceptanceConsecutive = consecutives.find(c => {
                    return c.documentType === 6;
                });
                if (this.partialAcceptanceConsecutive === undefined) {
                    this.partialAcceptanceConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 6,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }

                this.rejectionConsecutive = consecutives.find(c => {
                    return c.documentType === 7;
                });
                if (this.rejectionConsecutive === undefined) {
                    this.rejectionConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 7,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }

                this.buyingElectronicBillConsecutive = consecutives.find(c => {
                    return c.documentType === 8;
                });
                if (this.buyingElectronicBillConsecutive === undefined) {
                    this.buyingElectronicBillConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 8,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }

                this.exportElectronicBillingConsecutive = consecutives.find(c => {
                    return c.documentType === 9;
                });
                if (this.exportElectronicBillingConsecutive === undefined) {
                    this.exportElectronicBillingConsecutive = {
                        companyID: this.selectedCompany.id,
                        documentType: 9,
                        posID: this.selectedPos.id,
                        officeID: this.selectedOffice.office.id,
                        consecutive: 0,
                        documentKey: 0,
                    };
                }
            }
        }
    }

    public segmentChanged(event) {
        this.selectedSegment = event.detail.value;
    }

    public async saveConsecutives() {
        try {
            this.presentLoading();
            const postsToUpdate = [];

            this.electronicBillsConsecutive.consecutive = this.consecutivesForm.value.electronicBillsConsecutive;
            this.debitNotesConsecutive.consecutive = this.consecutivesForm.value.debitNotesConsecutive;
            this.creditNotesBillsConsecutive.consecutive = this.consecutivesForm.value.creditNotesBillsConsecutive;
            this.electronicTicketConsecutive.consecutive = this.consecutivesForm.value.electronicTicketConsecutive;
            this.acceptanceConfirmationConsecutive.consecutive = this.consecutivesForm.value.acceptanceConfirmationConsecutive;
            this.rejectionConsecutive.consecutive = this.consecutivesForm.value.rejectionConsecutive;
            this.partialAcceptanceConsecutive.consecutive = this.consecutivesForm.value.partialAcceptanceConsecutive;
            this.buyingElectronicBillConsecutive.consecutive = this.consecutivesForm.value.buyingElectronicBillConsecutive;
            this.exportElectronicBillingConsecutive.consecutive = this.consecutivesForm.value.exportElectronicBillingConsecutive;

            if (this.electronicBillsConsecutive.consecutive !== undefined && this.electronicBillsConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.electronicBillsConsecutive);
            }
            if (this.debitNotesConsecutive.consecutive !== undefined && this.debitNotesConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.debitNotesConsecutive);
            }
            if (this.creditNotesBillsConsecutive.consecutive !== undefined && this.creditNotesBillsConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.creditNotesBillsConsecutive);
            }
            if (this.electronicTicketConsecutive.consecutive !== undefined && this.electronicTicketConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.electronicTicketConsecutive);
            }
            if (this.acceptanceConfirmationConsecutive.consecutive !== undefined && this.acceptanceConfirmationConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.acceptanceConfirmationConsecutive);
            }
            if (this.rejectionConsecutive.consecutive !== undefined && this.rejectionConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.rejectionConsecutive);
            }
            if (this.partialAcceptanceConsecutive.consecutive !== undefined && this.partialAcceptanceConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.partialAcceptanceConsecutive);
            }
            if (this.buyingElectronicBillConsecutive.consecutive !== undefined && this.buyingElectronicBillConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.buyingElectronicBillConsecutive);
            }
            if (this.exportElectronicBillingConsecutive.consecutive !== undefined && this.exportElectronicBillingConsecutive.consecutive.length > 0) {
                postsToUpdate.push(this.exportElectronicBillingConsecutive);
            }

            await this.companyService.saveConsecutives(postsToUpdate);
            this.loadConsecutives();

            this.dismissLoading();
        } catch (e) {
            this.dismissLoading();
        }
    }


    public changeSelectedPos(event) {
        this.selectedPos = this.selectedOffice.pointsOfSale.find(i => {
            return i.id === event.detail.value;
        });

        this.loadConsecutives();
    }

    public changeSelectedOffice(event) {
        this.selectedOffice = this.offices.find(i => {
            return i.office.id === event.detail.value;
        });
        if (this.selectedOffice !== undefined) {
            this.selectedPos = this.selectedOffice.pointsOfSale[0];
        }

        if (this.selectedPos !== undefined) {
            this.loadConsecutives();
        }
    }

    public async loadOffices() {
        this.offices = await this.companyService.getOffices(this.selectedCompany.id);
        this.selectedOffice = this.offices[0];
        if (this.selectedOffice !== undefined) {
            this.selectedPos = this.selectedOffice.pointsOfSale[0];
        }
        this.loadConsecutives().then();
    }
}
