/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import { TestBed } from '@angular/core/testing';

import { ProvincesService } from './provinces.service';

describe('ProvincesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProvincesService = TestBed.get(ProvincesService);
    expect(service).toBeTruthy();
  });
});
