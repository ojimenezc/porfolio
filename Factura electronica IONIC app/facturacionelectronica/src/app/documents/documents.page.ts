import {Component, OnInit} from '@angular/core';
import {DocumentsService} from '../services/documents.service';
import {CompanyService} from '../services/company.service';
import {LoadingController} from '@ionic/angular';


@Component({
    selector: 'app-documents',
    templateUrl: './documents.page.html',
    styleUrls: ['./documents.page.scss'],
})
export class DocumentsPage implements OnInit {
    public selectedCompany: any;
    public companies;
    private loading: any;
    public filteredDocuments;
    public originalDocuments;
    public filterCriteria;
    private refreshAttempts = 0;

    constructor(private documentsService: DocumentsService,
                private loadingController: LoadingController,
                private companyService: CompanyService) {
    }

    async ionViewWillEnter() {
        await this.presentLoading();
        await this.loadCompanies();
        await this.loadDocuments();
        this.updateStatus().then();
        await this.dismissLoading();
        this.refreshInterval();
    }

    async ngOnInit() {

    }

    private async refreshInterval() {
        setInterval(() => {
            if (this.refreshAttempts <= 2) {
                this.loadDocuments().then();
                this.updateStatus().then();
                this.refreshAttempts += 1;
            }
        }, 10000);
    }

    private async loadDocuments() {

        if (this.selectedCompany !== undefined) {
            this.originalDocuments = await this.documentsService.get(this.selectedCompany.id);
        }
        this.filteredDocuments = this.originalDocuments.sort(this.comprarer);
    }

    private comprarer(a, b) {
        const dateA = new Date(a.generationDate);
        const dateB = new Date(b.generationDate);
        return dateB > dateA;
    }

    public getTotal(object): any {
        const obj = JSON.parse(atob(object.jsonbase64));
        let formatter;
        switch (obj.resumenFactura.codigoTipoMoneda.codigoMoneda) {
            case 'USD':
                formatter = new Intl.NumberFormat('en-US', {
                    style: 'currency',
                    currency: `${obj.resumenFactura.codigoTipoMoneda.codigoMoneda}`,
                });
                break;
            case 'CRC':
                formatter = new Intl.NumberFormat('es-CR', {
                    style: 'currency',
                    currency: `${obj.resumenFactura.codigoTipoMoneda.codigoMoneda}`,
                });
                break;
        }
        if (formatter !== undefined) {
            return formatter.format(obj.resumenFactura.totalComprobante);
        } else {
            return 0;
        }
    }

    public filterDocuments() {
        if (this.filterCriteria === null || this.filterCriteria.length === 0) {
            this.filteredDocuments = this.originalDocuments;
        } else {
            this.filteredDocuments = this.originalDocuments.filter(item => {
                return (item.documentKey.toLowerCase().includes(this.filterCriteria.toLocaleLowerCase()));
            }).sort(this.comprarer);
        }
    }

    private async updateStatus() {
        this.originalDocuments.forEach(async (d) => {
            if (d.statusIndicator === null || d.statusIndicator === undefined) {
                const docStatus = await this.documentsService.getDocStatus(d.documentKey);
            }
        });
    }

    public async changeCompany(event) {
        if (this.companies !== undefined) {
            this.selectedCompany = this.companies.find(c => {
                return c.id === event.detail.value;
            });

            this.loadDocuments();
        }
    }

    private async loadCompanies() {
        this.companies = await this.companyService.getAll();
        this.selectedCompany = this.companies[0];
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
}
