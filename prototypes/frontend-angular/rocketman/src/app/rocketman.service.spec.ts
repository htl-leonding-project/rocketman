import { TestBed } from '@angular/core/testing';

import { RocketmanService } from './rocketman.service';

describe('RocketmanService', () => {
  let service: RocketmanService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RocketmanService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
