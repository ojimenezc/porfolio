import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {StorageService} from './storage.service';
import {TokenService} from './token.service';
import {environment} from '../../environments/environment';
import {CacheService} from 'ionic-cache';

@Injectable({
    providedIn: 'root'
})
export class DistrictsService {

    constructor(private http: HttpClient, private router: Router,
                private cache: CacheService,
                private storage: StorageService, private tokenService: TokenService) {
    }

    public async get(countyId, province) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const request = this.http.get(`${environment.apiURI}/districts/get?countyId=${countyId}&provinceCode=${province}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        });

        return await this.cache.loadFromObservable('districts' + province + countyId, request).toPromise();
    }
}
