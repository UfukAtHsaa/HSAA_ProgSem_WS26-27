import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Greeting } from './greeting';
import { GreetingService } from './greeting.service';

describe('GreetingService', () => {
  let service: GreetingService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(GreetingService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('GETs /api/hello relative to the current origin', () => {
    let received: Greeting | undefined;
    service.hello().subscribe((greeting) => (received = greeting));

    const request = httpMock.expectOne('/api/hello');
    expect(request.request.method).toBe('GET');

    const payload: Greeting = { message: 'Hello, World!', servedAt: '2026-01-01T12:00:00Z' };
    request.flush(payload);

    expect(received).toEqual(payload);
  });
});