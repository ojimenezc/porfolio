/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {IdentificationTypesService} from '../services/identification-types.service';
import {ProvincesService} from '../services/provinces.service';
import {CountiesService} from '../services/counties.service';
import {DistrictsService} from '../services/districts.service';
import {ClientsService} from '../services/clients.service';
import {Router} from '@angular/router';
import {StorageService} from '../services/storage.service';
import {LoadingController} from '@ionic/angular';
import {TranslateConfigService} from "../services/translate-config.service";

@Component({
    selector: 'app-add-client',
    templateUrl: './add-client.page.html',
    styleUrls: ['./add-client.page.scss'],
})
export class AddClientPage implements OnInit {

    public addClientFormGroup = this.formBuilder.group({
        identificationType: new FormControl('', Validators.required),
        identificationNumber: new FormControl('', Validators.required),
        clientName: new FormControl('', Validators.required),
        contactPhone: new FormControl('', Validators.required),
        contactEmail: new FormControl('', Validators.compose([
            Validators.required,
            Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')
        ])),
        province: new FormControl('', Validators.required),
        county: new FormControl('', Validators.required),
        district: new FormControl('', Validators.required),
        otherSigns: new FormControl('', Validators.required)
    });

    public identificationTypes: any;
    public counties: any;
    public districts: any;
    public provinces: any;
    public saving: false;
    public loading: any;
    public error: boolean;

    constructor(private formBuilder: FormBuilder,
                private provincesService: ProvincesService,
                private countiesService: CountiesService,
                private districtsService: DistrictsService,
                private clientsService: ClientsService,
                private router: Router,
                private storage: StorageService,
                private loadingController: LoadingController,
                private identificationTypesService: IdentificationTypesService) {

    }

    ngOnInit() {
        this.error = false;
        this.loadIdentificationTypes();
        this.loadProvinces();
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

    async loadIdentificationTypes() {
        this.identificationTypes = await this.identificationTypesService.get();
    }

    async loadProvinces() {
        this.provinces = await this.provincesService.get();
    }

    async loadCounties() {
        this.counties = await this.countiesService.get(this.addClientFormGroup.value.province);
    }

    async loadDistricts() {
        this.districts = await this.districtsService.get(this.addClientFormGroup.value.county, this.addClientFormGroup.value.province);
    }

    public async save() {
        this.error = false;
        this.presentLoading();
        const user = await this.storage.getUserInfo();
        const request = {
            client: this.addClientFormGroup.value,
            customer: user.id
        };

        this.clientsService.save(request).then(result => {
            this.dismissLoading();
            this.router.navigateByUrl('clients');
        }).catch(err => {
            this.error = true;
        });
    }
}
