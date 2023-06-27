import {Component, OnInit} from '@angular/core';
import {KeycloakEventType, KeycloakService} from 'keycloak-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  public isAuthenticated = false;

  constructor(public authService: KeycloakService) {
    this.authService.keycloakEvents$.subscribe({
        next(event): void{
          if (event.type === KeycloakEventType.OnAuthSuccess){
            this.isAuthenticated = true;
          }else if (event.type === KeycloakEventType.OnAuthLogout || event.type === KeycloakEventType.OnTokenExpired){
            this.isAuthenticated = false;
          }
        }
      }
    );
  }

  async ngOnInit(): Promise<void> {
    this.isAuthenticated = await this.authService.isLoggedIn();
  }

  login(): void {
    this.authService.login( {
      prompt: 'login'
    });
  }

  logout(): void {
    this.authService.logout(window.location.origin);
  }
}
