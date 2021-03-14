/*
 * Copyright (c) 2019.  SOFTCORP-CR S.A. All Rights Reserved. Any partial or total reproduction of this file without authorization from the owner is prohibited.
 */

import { TestBed } from '@angular/core/testing';

import { AuthenticationService } from './authentication.service';

describe('AuthenticationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AuthenticationService = TestBed.get(AuthenticationService);
    expect(service).toBeTruthy();
  });
});
