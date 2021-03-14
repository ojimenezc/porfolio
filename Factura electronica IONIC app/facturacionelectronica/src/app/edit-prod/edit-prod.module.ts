import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Routes, RouterModule} from '@angular/router';

import {IonicModule} from '@ionic/angular';

import {EditProdPage} from './edit-prod.page';

import {TranslateModule} from "@ngx-translate/core";

const routes: Routes = [
    {
        path: '',
        component: EditProdPage
    }
];

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        IonicModule,
        TranslateModule,
        ReactiveFormsModule,
        RouterModule.forChild(routes)
    ],
    declarations: [EditProdPage]
})
export class EditProdPageModule {
}
