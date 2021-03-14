/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {NavController} from '@ionic/angular';
import {RxwebValidators} from '@rxweb/reactive-form-validators';
import {RegisterService} from '../services/register.service';
import {StorageService} from '../services/storage.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.page.html',
    styleUrls: ['./register.page.scss'],
})
export class RegisterPage implements OnInit {
    public registerForm = this.formBuilder.group({
        representativeName: new FormControl('', Validators.required),
        representativePhone: new FormControl('', Validators.required),
        representativeEmail: new FormControl('', Validators.compose([
            Validators.required,
            Validators.pattern('^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$')
        ])),
        username: new FormControl('', Validators.compose([
            Validators.required,
            Validators.minLength(8)
        ])),
        password: new FormControl('', Validators.compose([
            Validators.required,
            Validators.minLength(10)
        ])),
        passwordConfirm: new FormControl('', Validators.compose([
            Validators.required,
            RxwebValidators.compare({fieldName: 'password'})
        ]))
    });

    constructor(private navCtrl: NavController,
                private registerService: RegisterService,
                private storage: StorageService,
                private formBuilder: FormBuilder) {

    }

    ngOnInit() {
    }

    public async register(value) {
        if (this.registerForm.valid) {
            const result = await this.registerService.save(value);
            await this.storage.setUserInfo(result);
            this.navCtrl.navigateForward('home');
        }
    }

    public gotToLogin() {
        this.navCtrl.navigateForward('login');
    }

}
