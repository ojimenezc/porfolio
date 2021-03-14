import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivitiesService} from '../services/activities.service';
import {environment} from '../../environments/environment';
import {IonContent, LoadingController} from '@ionic/angular';
import {CompanyService} from "../services/company.service";
import {getTranslationConstPrefix} from "@angular/compiler/src/render3/view/i18n/util";


@Component({
    selector: 'app-economic-activities',
    templateUrl: './economic-activities.page.html',
    styleUrls: ['./economic-activities.page.scss'],
})
export class EconomicActivitiesPage implements OnInit {
    @ViewChild(IonContent, {static: true}) content: IonContent;
    public activities = [];
    public originalActivities = [];
    private offset = 0;
    public filterCriteria = '';
    private loading: any;
    private companyActivities;
    public companies;
    public selectedCompany;
    private updatingToggles = false;

    constructor(private activitiesService: ActivitiesService,
                private companiesService: CompanyService,
                private loadingController: LoadingController) {
        this.offset = 0;
    }

    async ngOnInit() {
        await this.presentLoading();
        await this.loadActivitiesSet(0);
        await this.loadCompanies();
        await this.dismissLoading();
    }

    async loadCompanies() {
        this.companies = await this.companiesService.getAll();
        this.selectedCompany = this.companies[0];
        this.loadCompanyActivities().then(() => {
            this.loadToggleChecks();
        });
    }

    async loadCompanyActivities() {
        if (this.selectedCompany !== undefined) {
            this.companyActivities = await this.activitiesService.getByCompany(this.selectedCompany.id);
        }
    }

    async loadToggleChecks() {
        if (this.companyActivities !== undefined) {
            const updatedActivities = [];
            this.originalActivities.forEach(a => {
                const existing = this.companyActivities.find(i => {
                    return i.activityCode === a.code;
                });

                a.checked = existing !== undefined;
                updatedActivities.push(a);
            });

            this.activities = updatedActivities;
        }
    }

    public async changeCompany(event) {
        this.selectedCompany = this.companies.find(c => {
            return c.id === event.detail.value;
        });
        this.loadCompanyActivities().then(() => {
            this.loadToggleChecks();
        });
    }


    async filter() {
        if (this.filterCriteria === undefined || this.filterCriteria.length === 0) {
            this.activities = this.originalActivities;
        } else {
            this.activities = this.originalActivities.filter(activity => {
                return activity.name.toLocaleLowerCase().includes(this.filterCriteria.toLocaleLowerCase())
                    || activity.code.includes(this.filterCriteria.toLocaleLowerCase());
            });
        }
    }


    async loadActivitiesSet(offset) {
        try {
            let act: any = [];
            act = await this.activitiesService.get(offset, environment.pageSize);
            act.forEach(i => {
                i.checked = false;
                this.originalActivities.push(i);
            });
            this.activities = this.originalActivities;
        } catch (e) {
            console.log(JSON.stringify(e));
        }
    }

    scrollToBottom() {
        this.content.scrollToBottom(500);
    }

    scrollToTop() {
        this.content.scrollToTop(500);
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

    async toggleForCompany(event) {
        if (event.detail.checked === true) {
            const existing = this.companyActivities.find(a => {
                return a.activityCode === event.detail.value;
            })
            if (existing === undefined) {
                await this.activitiesService.addToCompany({
                    companyId: this.selectedCompany.id,
                    activityCode: event.detail.value
                });
            }
        } else {
            await this.activitiesService.removeFromCompany(this.selectedCompany.id, event.detail.value);
        }

        this.loadCompanyActivities().then(() => {
            this.loadToggleChecks();
        });
    }
}
