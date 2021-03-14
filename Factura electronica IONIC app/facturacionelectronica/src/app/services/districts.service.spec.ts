import { TestBed } from '@angular/core/testing';

import { DistrictsService } from './districts.service';

describe('DistrictsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DistrictsService = TestBed.get(DistrictsService);
    expect(service).toBeTruthy();
  });
});
