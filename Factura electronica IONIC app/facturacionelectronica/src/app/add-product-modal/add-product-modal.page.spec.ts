import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddProductModalPage } from './add-product-modal.page';

describe('AddProductModalPage', () => {
  let component: AddProductModalPage;
  let fixture: ComponentFixture<AddProductModalPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddProductModalPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddProductModalPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
