/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from "./storage.service";
import {environment} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class TokenService {

    private apiTokenRequest = {
        client_id: 'facturacion.api',
        client_secret: 'oIZJGUvO7VaCjxmfLfnCDl4HNwBrPISJ',
        grant_type: 'client_credentials'
    };

    constructor(private http: HttpClient, private storage: StorageService) {

    }

    public async getAPIToken() {
        const options = new HttpHeaders({});
        options.append('Content-Type', 'application/json');
        const tokenObject = await this.http.post(environment.idpUri, this.apiTokenRequest, {headers: options}).toPromise();
        this.storage.set('access_token', tokenObject);
        return tokenObject;
    }
}
