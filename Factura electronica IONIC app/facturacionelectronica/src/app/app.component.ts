/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Component} from '@angular/core';

import {Platform} from '@ionic/angular';
import {SplashScreen} from '@ionic-native/splash-screen/ngx';
import {StatusBar} from '@ionic-native/status-bar/ngx';
import {Router} from '@angular/router';
import {AuthenticationService} from './services/authentication.service';
import {TranslateConfigService} from './services/translate-config.service';
import {StorageService} from './services/storage.service';
import {CacheService} from 'ionic-cache';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
    styleUrls: ['app.component.scss'],

})
export class AppComponent {
    public appPages = [
        {
            title: 'INVOICING_MENU_TITLE',
            url: '',
            showDetails: false,
            subitems: [{
                title: 'NEW_DOCUMENT',
                url: '/invoicing',
                icon: 'add'
            },
                {
                    title: 'LIST_DOCUMENTS',
                    url: '/documents',
                    icon: 'copy',
                    showDetails: false,
                    subitems: []
                }]
        },
        {
            title: 'CLIENTS_MENU_ITEM',
            url: '/clients',
            icon: 'people',
            showDetails: false,
            subitems: []
        },
        {
            title: 'ECONOMIC_ACTIVITIES',
            url: '/economic-activities',
            icon: 'cash',
            showDetails: false,
            subitems: []
        },
        {
            title: 'PRODUCTS',
            url: '/products',
            icon: 'pricetags',
            showDetails: false,
            subitems: []
        },
        {
            title: 'COMPANY_SETTINGS',
            url: '/company',
            icon: 'cog',
            showDetails: false,
            subitems: []
        }
    ];

    public selectedLanguage;

    constructor(
        private platform: Platform,
        private splashScreen: SplashScreen,
        private statusBar: StatusBar,
        private router: Router,
        private storage: StorageService,
        private translateConfig: TranslateConfigService,
        private auth: AuthenticationService,
        private cache: CacheService
    ) {
        this.initializeApp();
        cache.setDefaultTTL(60 * 60);
    }

    initializeApp() {
        this.platform.ready().then(() => {
            const targetPath = window.location.pathname.replace('/', '').trim();
            console.log('Ready', targetPath);
            this.statusBar.styleDefault();
            this.splashScreen.hide();
            this.auth.isAuthenticated();
            this.auth.authState.subscribe(state => {
                console.log('State', state);
                console.log('Target', targetPath);
                if (state) {
                    this.router.navigate([targetPath]);
                } else {
                    this.router.navigate(['login']);
                }
            });
        });

        this.selectedLanguage = this.translateConfig.getDefaultLanguage();
        this.translateConfig.setLanguage(this.selectedLanguage);
    }

    public changeLanguage() {
        this.translateConfig.setLanguage(this.selectedLanguage);
    }

    public logout() {
        this.storage.removeUserInfo();
        this.router.navigateByUrl('login');
    }


    public toggleSubitems(item) {
        item.showDetails = !item.showDetails;
        const element = document.getElementById('subitems_' + item.title);
        if (item.showDetails === true) {
            element.style.height = '90px';
        } else {
            element.style.height = '0px';
        }
    }

    public getSingleItems() {
        return this.appPages.filter(i => {
            return i.subitems.length === 0;
        });
    }

    public getGroupItems() {
        return this.appPages.filter(i => {
            return i.subitems.length > 0;
        });
    }
}
