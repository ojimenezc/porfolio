/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouteReuseStrategy} from '@angular/router';

import {IonicModule, IonicRouteStrategy, Platform} from '@ionic/angular';
import {SplashScreen} from '@ionic-native/splash-screen/ngx';
import {StatusBar} from '@ionic-native/status-bar/ngx';

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {TokenService} from './services/token.service';
import {HttpClientModule} from '@angular/common/http';
import {AuthGuardService} from './services/auth-guard.service';
import {AuthenticationService} from './services/authentication.service';
import {IonicStorageModule} from '@ionic/storage';
import {StorageService} from './services/storage.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RegisterService} from './services/register.service';
import {IdentificationTypesService} from './services/identification-types.service';
import {DistrictsService} from './services/districts.service';
import {NeighborhoodsService} from './services/neighborhoods.service';
import {CountiesService} from './services/counties.service';
import {HttpClient} from '@angular/common/http';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {Chooser} from '@ionic-native/chooser/ngx';
import {AppVersion} from '@ionic-native/app-version/ngx';
import {LoggerService} from './services/logger.service';
import {CacheModule} from 'ionic-cache';
import {AddProductModalPageModule} from './add-product-modal/add-product-modal.module';

export function LanguageLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

@NgModule({
    declarations: [AppComponent],
    entryComponents: [],
    imports: [
        BrowserModule,
        IonicModule.forRoot(),
        AppRoutingModule,
        HttpClientModule,
        ReactiveFormsModule,
        CacheModule.forRoot(),
        AddProductModalPageModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: (LanguageLoader),
                deps: [HttpClient]
            }
        }),
        IonicStorageModule.forRoot(),
        FormsModule,
    ],
    providers: [
        StatusBar,
        Chooser,
        SplashScreen,
        TokenService, AuthenticationService,
        AuthGuardService,
        StorageService,
        RegisterService,
        IdentificationTypesService,
        DistrictsService,
        NeighborhoodsService,
        CountiesService,
        Platform,
        AppVersion,
        LoggerService,
        {provide: RouteReuseStrategy, useClass: IonicRouteStrategy}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
