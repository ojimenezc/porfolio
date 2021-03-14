import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditClientPage } from './edit-client.page';

describe('EditClientPage', () => {
  let component: EditClientPage;
  let fixture: ComponentFixture<EditClientPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditClientPage ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditClientPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
