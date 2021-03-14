/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Injectable} from '@angular/core';
import {Storage} from '@ionic/storage';

@Injectable({
    providedIn: 'root'
})
export class StorageService {

    constructor(private storage: Storage) {
    }

    public getUserInfo() {
        return this.storage.get('USER_INFO');
    }

    public removeUserInfo() {
        return this.storage.remove('USER_INFO');
    }

    public setUserInfo(data) {
        return this.storage.set('USER_INFO', data);
    }

    public set(key, value) {
        this.storage.set(key, value);
    }

    public async get(key) {
        return this.storage.get(key);
    }

    public remove(key) {
        this.storage.remove(key);
    }
}
