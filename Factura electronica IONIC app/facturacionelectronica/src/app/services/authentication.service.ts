/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';

import {BehaviorSubject} from 'rxjs';
import {Router} from '@angular/router';
import {StorageService} from './storage.service';
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    authState = new BehaviorSubject(false);

    constructor(private tokenService: TokenService,
                private router: Router,
                private storage: StorageService,
                private http: HttpClient) {
    }

    public async login(user, pwd) {
        try {
            const token = await this.tokenService.getAPIToken();
            const tokenResponse = JSON.parse(JSON.stringify(token));
            const userInfo = await this.http.get(`${environment.apiURI}/customers/login?username=${user}&password=${pwd}`, {
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ` + tokenResponse.access_token
                }),
            }).toPromise();
            if (userInfo !== undefined) {
                await this.storage.setUserInfo(userInfo);
                this.authState.next(true);
                return true;
            }

            return false;

        } catch (e) {
            console.log('Error loggin in user', e);
        }
    }


    ifLoggedIn() {
        this.storage.getUserInfo().then((response) => {
            if (response) {
                this.authState.next(true);
            }
        });
    }

    logout() {
        this.storage.removeUserInfo().then(() => {
            this.router.navigate(['login']);
            this.authState.next(false);
        });
    }

    async isAuthenticated() {
        const userInfo = await this.storage.getUserInfo();
        const valid = userInfo !== null;
        this.authState.next(valid);
        return valid;
    }
}
