import { TestBed } from '@angular/core/testing';

import { TaxesService } from './taxes.service';

describe('TaxesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TaxesService = TestBed.get(TaxesService);
    expect(service).toBeTruthy();
  });
});
