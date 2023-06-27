import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot} from '@angular/router';
import {KeycloakAuthGuard, KeycloakService} from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class CustomRouterAuthGuard extends KeycloakAuthGuard {
  constructor(protected readonly router: Router, protected readonly authService: KeycloakService) {
    super(router, authService);
  }

  public async isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    // Force the user to log in if currently unauthenticated.
    if (!this.authenticated) {
      await this.authService.login({
        redirectUri: window.location.origin + state.url
      });
    }
    return true;
  }
}
