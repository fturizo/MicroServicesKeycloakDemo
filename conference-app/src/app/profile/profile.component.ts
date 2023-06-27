import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  name: string;
  email: string;
  idToken: string;
  constructor(public authService: KeycloakService) { }

  async ngOnInit(): Promise<void> {
    const profile = await this.authService.getKeycloakInstance().loadUserProfile();
    this.name = `${profile.firstName} ${profile.lastName}`;
    this.email = profile.email;
    this.idToken = JSON.stringify(profile, null, '  ');
  }
}
