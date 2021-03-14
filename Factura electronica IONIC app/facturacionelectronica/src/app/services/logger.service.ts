import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class LoggerService {

    private endpoint = 'http://logs-01.loggly.com/inputs/5b6aa91c-8a3f-4949-97e0-6facebfc9091/tag/http/';

    constructor(private http: HttpClient) {
    }

    public async error(message) {
        return this.http.post(this.endpoint, message, {
            headers: new HttpHeaders({
                'Content-Type': 'application/json'
            })
        });
    }
}
