import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TokenService} from './token.service';
import {StorageService} from './storage.service';

@Injectable({
    providedIn: 'root'
})
export class ProductsService {

    constructor(private http: HttpClient, private storage: StorageService, private tokenService: TokenService) {
    }

    public async save(product) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        const savedData = await this.http.post(`${environment.apiURI}/products/save`, product, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();

        return savedData;
    }

    public async get(customerId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/products/get?customerId=${customerId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }


    public async getsingle(id) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/products/getsingle?id=${id}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }
}
