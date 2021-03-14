import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from './storage.service';
import {TokenService} from './token.service';
import {environment} from '../../environments/environment';
import {CacheService} from 'ionic-cache';

@Injectable({
    providedIn: 'root'
})
export class ActivitiesService {

    constructor(private http: HttpClient,
                private cache: CacheService,
                private storage: StorageService, private tokenService: TokenService) {
    }

    public async get(offset, size) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const request = this.http.get(`${environment.apiURI}/activities/get?offset=${offset}&pageSize=${size}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        });

        return await this.cache.loadFromObservable('activities', request).toPromise();
    }

    public async getByCompany(companyId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/activities/getbycompany?companyId=${companyId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }

    public async addToCompany(data) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const savedData = await this.http.post(`${environment.apiURI}/activities/addtocompany`, data, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();

        return savedData;
    }

    public async removeFromCompany(companyId, code) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const savedData = await this.http.delete(`${environment.apiURI}/activities/removefromcompany?companyId=${companyId}&code=${code}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();

        return savedData;
    }
}
