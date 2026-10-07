import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Greeting } from './greeting';

@Injectable({ providedIn: 'root' })
export class GreetingService {
  private readonly http = inject(HttpClient);

  // Relative URL on purpose: nginx proxies /api to the backend container,
  // so the app needs to know nothing about where the backend lives and no CORS applies.
  hello(): Observable<Greeting> {
    return this.http.get<Greeting>('/api/hello');
  }
}