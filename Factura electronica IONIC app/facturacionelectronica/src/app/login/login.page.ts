/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Component, OnInit} from '@angular/core';
import {MenuController, NavController} from '@ionic/angular';
import {AuthenticationService} from '../services/authentication.service';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {LoggerService} from "../services/logger.service";

@Component({
    selector: 'app-login',
    templateUrl: './login.page.html',
    styleUrls: ['./login.page.scss'],
})
export class LoginPage implements OnInit {

    public loginForm = new FormGroup({
        username: new FormControl('', Validators.required),
        pwd: new FormControl('', Validators.required)
    });
    public version;
    public loggedIn = undefined;
    public loading = false;

    constructor(private menuCtrl: MenuController,
                private router: Router,
                private navCtrl: NavController,
                private logger: LoggerService,
                private auth: AuthenticationService) {

    }

    ionViewWillEnter() {
        this.auth.isAuthenticated().then(r => {
            this.menuCtrl.enable(r);
        });
    }

    ngOnInit() {
    }

    public async login(value) {
        try {
            this.loading = true;
            const result = await this.auth.login(value.username, value.pwd);
            if (result === true) {
                this.loggedIn = true;
                this.auth.isAuthenticated().then(r => {
                    this.menuCtrl.enable(r);
                    this.router.navigateByUrl('home');
                });
            } else {
                this.loggedIn = false;
            }
            this.loading = false;
        } catch (e) {
            this.loggedIn = false;
            this.logger.error(JSON.stringify(e)).then();
        }
    }

    public goToRegister() {
        this.navCtrl.navigateForward('register');
    }
}
