/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import {NgModule} from '@angular/core';
import {PreloadAllModules, RouterModule, Routes} from '@angular/router';
import {AuthGuardService} from './services/auth-guard.service';

const routes: Routes = [
    {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        loadChildren: () => import('./home/home.module').then(m => m.HomePageModule),

    },
    {
        path: 'list',
        loadChildren: () => import('./list/list.module').then(m => m.ListPageModule),

    },
    {path: 'login', loadChildren: './login/login.module#LoginPageModule'},
  { path: 'register', loadChildren: './register/register.module#RegisterPageModule' },
  { path: 'clients',  loadChildren: './clients/clients.module#ClientsPageModule' },
  { path: 'add-client', loadChildren: './add-client/add-client.module#AddClientPageModule' },
  { path: 'edit-client', loadChildren: './edit-client/edit-client.module#EditClientPageModule' },
  { path: 'company', loadChildren: './company/company.module#CompanyPageModule' },
  { path: 'add-company', loadChildren: './add-company/add-company.module#AddCompanyPageModule' },
  { path: 'products', loadChildren: './products/products.module#ProductsPageModule' },
  { path: 'add-product', loadChildren: './add-product/add-product.module#AddProductPageModule' },
  { path: 'edit-prod', loadChildren: './edit-prod/edit-prod.module#EditProdPageModule' },
  { path: 'economic-activities', loadChildren: './economic-activities/economic-activities.module#EconomicActivitiesPageModule' },
  { path: 'invoicing', loadChildren: './invoicing/invoicing.module#InvoicingPageModule' },
  { path: 'add-product-modal', loadChildren: './add-product-modal/add-product-modal.module#AddProductModalPageModule' },
  { path: 'documents', loadChildren: './documents/documents.module#DocumentsPageModule' }
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes, {preloadingStrategy: PreloadAllModules})
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
