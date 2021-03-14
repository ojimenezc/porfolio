import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {StorageService} from './storage.service';
import {TokenService} from './token.service';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CompanyService {

    constructor(private http: HttpClient, private router: Router,
                private storage: StorageService, private tokenService: TokenService) {
    }

    public async getAll() {
        const userInfo = await this.storage.getUserInfo();
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/companies/get?customerId=${userInfo.id}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }

    public async get(companyId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/companies/customerId?companyId=${companyId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }

    public async save(data) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const savedData = await this.http.post(`${environment.apiURI}/companies/save`, data, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();

        return savedData;
    }

    public async delete(companyId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const savedData = await this.http.delete(`${environment.apiURI}/companies/delete?companyId=${companyId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();

        return savedData;
    }

    public async getConsecutives(companyId, officeId, posId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/consecutives/get?companyId=${companyId}&officeId=${officeId}&posId=${posId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }

    public async getOffices(companyId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/offices/get?companyId=${companyId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }

    public async saveConsecutives(listToUpdate) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const savedData = await this.http.put(`${environment.apiURI}/consecutives/updatelist`, listToUpdate, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();

        return savedData;
    }

}
