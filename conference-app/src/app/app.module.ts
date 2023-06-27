import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import config from './app.config';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { SpeakersComponent } from './speakers/speakers.component';
import { SessionsComponent } from './sessions/sessions.component';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { MessagesComponent } from './messages/messages.component';
import { HIGHLIGHT_OPTIONS, HighlightModule } from 'ngx-highlightjs';
import { CustomRouterAuthGuard} from './router-guard';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'profile', component: ProfileComponent, canActivate: [CustomRouterAuthGuard]},
  {path: 'sessions', component: SessionsComponent, canActivate: [CustomRouterAuthGuard]},
  {path: 'speakers', component: SpeakersComponent, canActivate: [CustomRouterAuthGuard]}
];

// tslint:disable-next-line:typedef
function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config : config.keycloakConfig,
      initOptions: {
        onLoad: 'check-sso',
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
        enableLogging : false
      }
    });
}

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ProfileComponent,
    SpeakersComponent,
    SessionsComponent,
    MessagesComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    KeycloakAngularModule,
    NgbModule,
    HighlightModule
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory : initializeKeycloak,
      multi : true,
      deps : [KeycloakService]
    },
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        fullLibraryLoader: () => import('highlight.js')
      }
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
