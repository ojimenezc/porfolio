/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Injectable} from '@angular/core';
import {AuthenticationService} from './authentication.service';
import {CanActivate} from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(private auth: AuthenticationService) {
    }

    canActivate() {
        return this.auth.isAuthenticated();
    }
}
