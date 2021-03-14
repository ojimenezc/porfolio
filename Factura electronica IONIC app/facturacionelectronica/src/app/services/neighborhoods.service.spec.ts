import { TestBed } from '@angular/core/testing';

import { NeighborhoodsService } from './neighborhoods.service';

describe('NeighborhoodsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NeighborhoodsService = TestBed.get(NeighborhoodsService);
    expect(service).toBeTruthy();
  });
});
