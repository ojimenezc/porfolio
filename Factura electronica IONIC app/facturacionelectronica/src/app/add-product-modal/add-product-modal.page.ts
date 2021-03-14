import {Component, OnInit} from '@angular/core';
import {ModalController} from '@ionic/angular';

@Component({
    selector: 'app-add-product-modal',
    templateUrl: './add-product-modal.page.html',
    styleUrls: ['./add-product-modal.page.scss'],
})
export class AddProductModalPage implements OnInit {
    public products;
    public selectedProduct;
    public editItem: any;
    public quantity = 1;
    public editing = false;

    constructor(private modalController: ModalController) {
    }

    ngOnInit() {
        if (this.products !== undefined && this.editItem === undefined) {
            this.selectedProduct = this.products[0];
            this.editing = false;
        }

        if (this.editItem !== undefined) {
            this.editing = true;
            this.selectedProduct = this.editItem.product;
            this.quantity = this.editItem.quantity;
        }
    }

    public changeSelectedProduct(event) {
        if (this.products !== undefined) {
            this.selectedProduct = this.products.find(i => {
                return i.id === event.detail.value;
            });
        }
    }

    async closeModal() {
        await this.modalController.dismiss({
            product: this.selectedProduct,
            quantity: this.quantity,
            editing: this.editing
        });
    }
}
