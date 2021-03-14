import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {CacheService} from 'ionic-cache';
import {StorageService} from './storage.service';
import {TokenService} from './token.service';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class DocumentTypesService {

    constructor(private http: HttpClient, private router: Router,
                private cache: CacheService,
                private storage: StorageService, private tokenService: TokenService) {
    }

    public async get() {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const request = this.http.get(`${environment.apiURI}/documentTypes/getAll`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        });

        return await this.cache.loadFromObservable('document_types', request).toPromise();
    }
}
