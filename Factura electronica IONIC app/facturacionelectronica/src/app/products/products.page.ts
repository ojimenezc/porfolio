import {Component, OnInit} from '@angular/core';
import {AlertController, LoadingController, NavController} from "@ionic/angular";
import {StorageService} from "../services/storage.service";
import {ProductsService} from "../services/products.service";
import {Router} from "@angular/router";
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-products',
    templateUrl: './products.page.html',
    styleUrls: ['./products.page.scss'],
})
export class ProductsPage implements OnInit {

    public products;
    private loading;

    constructor(private navigation: NavController,
                private productsService: ProductsService,
                private loadingController: LoadingController,
                private router: Router,
                private alertController: AlertController,
                private translate: TranslateService,
                private storage: StorageService) {
    }

    async ngOnInit() {
    }

    async ionViewDidEnter() {
        await this.presentLoading();
        await this.loadProducts();
        await this.dismissLoading();
    }

    async loadProducts() {
        try {
            const userInfo = await this.storage.getUserInfo();
            this.products = await this.productsService.get(userInfo.id);
        } catch (e) {
            console.log(JSON.stringify(e));
        }
    }

    public goToCreateProd() {
        this.navigation.navigateForward('add-product');
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

    async edit(prod) {
        this.storage.set('edit_prod', prod);
        this.navigation.navigateForward('edit-prod');
    }

}
