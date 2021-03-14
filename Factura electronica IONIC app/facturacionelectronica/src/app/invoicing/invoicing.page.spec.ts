import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoicingPage } from './invoicing.page';

describe('InvoicingPage', () => {
  let component: InvoicingPage;
  let fixture: ComponentFixture<InvoicingPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InvoicingPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoicingPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
