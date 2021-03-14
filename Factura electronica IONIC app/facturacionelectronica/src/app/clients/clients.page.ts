/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Component, OnInit} from '@angular/core';
import {AlertController, LoadingController, NavController} from '@ionic/angular';
import {StorageService} from '../services/storage.service';
import {ClientsService} from '../services/clients.service';
import {Router} from '@angular/router';
import {globals} from '../globals';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-clients',
    templateUrl: './clients.page.html',
    styleUrls: ['./clients.page.scss'],
})
export class ClientsPage implements OnInit {

    public clients: Array<ClientDTO> = [];
    public originalClientsList: Array<ClientDTO> = [];
    public filterCriteria: '';
    public notResultsFound: boolean;
    private loading: any;

    constructor(private navCtrl: NavController,
                private router: Router,
                private alertController: AlertController,
                private loadingController: LoadingController,
                private translate: TranslateService,
                private storage: StorageService, private clientsService: ClientsService) {
    }

    async ngOnInit() {

    }

    async ionViewWillEnter() {
        await this.presentLoading();
        await this.loadClients();
        await this.dismissLoading();
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

    private async loadClients() {
        const info = await this.storage.getUserInfo();
        const clientsString = await this.clientsService.get(info.id);
        this.originalClientsList = [];
        this.originalClientsList = JSON.parse(JSON.stringify(clientsString));
        this.clients = [];

        this.clients = this.originalClientsList;
    }

    public goToCreateClient() {
        this.navCtrl.navigateForward('add-client');
    }

    public filterClients() {
        if (this.filterCriteria === null || this.filterCriteria.length === 0) {
            this.clients = this.originalClientsList;
            this.notResultsFound = false;
        } else {
            this.clients = this.originalClientsList.filter(item => {
                return (item.clientName.toLowerCase().includes(this.filterCriteria.toLocaleLowerCase())
                    || item.contactEmail.toLowerCase().includes(this.filterCriteria.toLowerCase())
                    || item.identificationNumber.includes(this.filterCriteria.toLowerCase()));
            });

            this.notResultsFound = (this.clients.length === 0);
        }
    }

    public edit(value) {
        this.storage.set(globals.CLIENT_EDIT, value);
        this.router.navigateByUrl('edit-client');
    }

    public async delete(clientId) {


        const alertMessage = await this.translate.get('DELETE_CLIENT').toPromise();
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
                        await this.clientsService.delete(clientId);
                        this.loadClients();
                    }
                }
            ]
        });

        await alert.present();
    }
}
