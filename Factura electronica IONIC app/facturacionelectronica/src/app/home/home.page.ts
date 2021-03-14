/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {Component} from '@angular/core';
import {AppVersion} from '@ionic-native/app-version/ngx';
import {LoggerService} from '../services/logger.service';

@Component({
    selector: 'app-home',
    templateUrl: 'home.page.html',
    styleUrls: ['home.page.scss'],
})
export class HomePage {

    public version;

    constructor(private appVersion: AppVersion, private logger: LoggerService) {

        this.appVersion.getVersionNumber().then(v => {
            this.version = v;
        }).catch(err => {
            this.logger.error('Error getting version ' + JSON.stringify(err)).then();
        });
    }
}
