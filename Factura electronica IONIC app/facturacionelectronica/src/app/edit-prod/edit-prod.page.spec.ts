import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProdPage } from './edit-prod.page';

describe('EditProdPage', () => {
  let component: EditProdPage;
  let fixture: ComponentFixture<EditProdPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditProdPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditProdPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
