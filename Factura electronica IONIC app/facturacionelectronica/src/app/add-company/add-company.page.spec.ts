import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCompanyPage } from './add-company.page';

describe('AddCompanyPage', () => {
  let component: AddCompanyPage;
  let fixture: ComponentFixture<AddCompanyPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddCompanyPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCompanyPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
