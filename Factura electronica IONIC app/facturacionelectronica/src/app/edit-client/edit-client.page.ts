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
import {ActivatedRoute, Router} from '@angular/router';
import {StorageService} from '../services/storage.service';
import {LoadingController} from '@ionic/angular';
import {globals} from '../globals';

@Component({
    selector: 'edit-client',
    templateUrl: './edit-client.page.html',
    styleUrls: ['./edit-client.page.scss'],
})
export class EditClientPage implements OnInit {

    public editClientFormGroup = this.formBuilder.group({
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

    public identificationTypes: any[];
    public counties: any[];
    public districts: any[];
    public provinces: any[];
    public saving: false;
    public loading: any;
    public error: boolean;
    public client: any;
    public indentificationTypeName = '';
    public provinceName = '';
    public countyName = '';
    public districtName = '';

    constructor(private formBuilder: FormBuilder,
                private provincesService: ProvincesService,
                private countiesService: CountiesService,
                private districtsService: DistrictsService,
                private clientsService: ClientsService,
                private router: Router,
                private storage: StorageService,
                private activatedRoute: ActivatedRoute,
                private loadingController: LoadingController,
                private identificationTypesService: IdentificationTypesService) {

    }

    async ngOnInit() {
        this.error = false;
        await this.loadEditClient();
        this.loadIdentificationTypes();
        this.loadProvinces();
        this.loadCounties();
        this.loadDistricts();
    }

    async loadEditClient() {
        this.client = await this.storage.get(globals.CLIENT_EDIT);

        this.editClientFormGroup = this.formBuilder.group({
            id: new FormControl(this.client.id),
            identificationType: new FormControl(this.client.identificationType, Validators.required),
            identificationNumber: new FormControl(this.client.identificationNumber, Validators.required),
            clientName: new FormControl(this.client.clientName, Validators.required),
            contactPhone: new FormControl(this.client.contactPhone, Validators.required),
            contactEmail: new FormControl(this.client.contactEmail, Validators.compose([
                Validators.required,
                Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')
            ])),
            province: new FormControl(this.client.province, Validators.required),
            county: new FormControl(this.client.county, Validators.required),
            district: new FormControl(this.client.district, Validators.required),
            otherSigns: new FormControl(this.client.otherSigns, Validators.required)
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

    async loadIdentificationTypes() {
        const json = JSON.stringify(await this.identificationTypesService.get());
        this.identificationTypes = JSON.parse(json);
        this.loadSelectedIdTypeName();
    }

    private loadSelectedIdTypeName() {
        const selectedIdentificationType = this.identificationTypes.find(item => {
            return item.code === this.client.identificationType;
        });

        if (selectedIdentificationType !== undefined && selectedIdentificationType != null) {
            this.indentificationTypeName = selectedIdentificationType.name;
        }
    }

    async loadProvinces() {
        const json = JSON.stringify(await this.provincesService.get());
        this.provinces = JSON.parse(json);
        this.loadSelectedProvinceName();
    }

    public loadSelectedProvinceName() {

        const selectedProvince = this.provinces.find(item => {
            return item.code === this.client.province;
        });


        if (selectedProvince !== undefined && selectedProvince !== null) {
            this.provinceName = selectedProvince.name;
        }
    }

    public loadSelectedCountyName() {

        const selected = this.counties.find(item => {
            return item.code === this.client.district;
        });


        if (selected !== undefined && selected !== null) {
            this.countyName = selected.name;
        }
    }

    public loadSelectedDistrictName() {

        const selected = this.districts.find(item => {
            return item.code === this.client.district;
        });


        if (selected !== undefined && selected !== null) {
            this.districtName = selected.name;
        }
    }

    async loadCounties() {
        const json = JSON.stringify(await this.countiesService.get(this.editClientFormGroup.value.province));
        this.counties = JSON.parse(json);
        this.loadSelectedCountyName();
    }

    async loadDistricts() {
        const json = JSON.stringify(await this.districtsService.get(this.editClientFormGroup.value.county, this.editClientFormGroup.value.province));
        this.districts = JSON.parse(json);
        this.loadSelectedDistrictName();
    }

    public async save() {
        this.error = false;
        this.presentLoading();
        const user = await this.storage.getUserInfo();
        const request = {
            client: this.editClientFormGroup.value,
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
