import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { Greeting } from './greeting';
import { GreetingService } from './greeting.service';

@Component({
  imports: [DatePipe],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  private readonly greetingService = inject(GreetingService);

  protected readonly greeting = signal<Greeting | null>(null);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly loading = signal(false);

  constructor() {
    this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.greetingService.hello().subscribe({
      next: (greeting) => {
        this.greeting.set(greeting);
        this.loading.set(false);
      },
      error: () => {
        this.greeting.set(null);
        this.errorMessage.set('Could not reach the backend at /api/hello.');
        this.loading.set(false);
      },
    });
  }
}