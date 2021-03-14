import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {StorageService} from './storage.service';
import {TokenService} from './token.service';
import {environment} from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class DocumentsService {

    constructor(private http: HttpClient,
                private storage: StorageService, private tokenService: TokenService) {
    }

    public async get(companyId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/companies/documents?companyId=${companyId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }

    public async save(invoice) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));

        let endpoint = '';

        switch (invoice.documentType) {
            case '01':
                endpoint = '/invoices/generate';
                break;
            case '04':
                endpoint = '/tickets/generate';
                break;
            case '08':
                endpoint = '/purchaseinvoice/generate';
                break;
        }

        const savedData = await this.http.post(`${environment.apiURI}${endpoint}`, invoice, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();

        return savedData;
    }

    public async getDocStatus(documentId) {
        const token = await this.tokenService.getAPIToken();
        const tokenResponse = JSON.parse(JSON.stringify(token));
        return await this.http.get(`${environment.apiURI}/status/request?documentId=${documentId}`, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                Authorization: `Bearer ` + tokenResponse.access_token
            }),
        }).toPromise();
    }


}
