import { Component, OnInit } from '@angular/core';
import config from '../app.config';
import {KeycloakService} from 'keycloak-angular';
import {HttpClient} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {MessagesService} from '../messages.service';
import {KeycloakProfile} from 'keycloak-js';

export interface Speaker{
  id: number;
  name: string;
  organization: string;
  accepted: boolean;
  identity: string;
}

@Component({
  selector: 'app-speakers',
  templateUrl: './speakers.component.html',
  styleUrls: ['./speakers.component.css']
})
export class SpeakersComponent implements OnInit {

  speakers: Array<Speaker>;
  isUserRegistered: boolean;
  currentModal: NgbModalRef;
  profile: KeycloakProfile;
  registerForm;
  constructor(public authService: KeycloakService,
              private http: HttpClient,
              private formBuilder: FormBuilder,
              private modalService: NgbModal,
              private messagesService: MessagesService) {
    this.speakers = [];
    this.registerForm = formBuilder.group({
      name: '',
      organization: '',
    });
  }

  async ngOnInit(): Promise<void> {
    this.profile = await this.authService.getKeycloakInstance().loadUserProfile();
    this.http.get(config.serviceURLs.speaker + 'speaker/all', {})
      .subscribe(
        (data: Array<Speaker>) => {
          [].push.apply(this.speakers, data);
          this.checkIfUserRegistered();
        },
        error => {
          if (error.status === 403) {
            this.messagesService.addMessage('danger', 'Not authorized to view speakers');
          }
        });
  }

  private async checkIfUserRegistered(): Promise<void>{
    const userName = this.profile.username;
    this.isUserRegistered = this.speakers.findIndex(speaker => speaker.identity === userName) > -1;
  }

  async acceptSpeaker(speaker: Speaker): Promise<void> {
    const component = this;
    this.http.post(config.serviceURLs.speaker + `speaker/accept/${speaker.id}`, {}, {})
      .subscribe({
        next(): void {
          component.resetSpeakers();
          component.messagesService.addMessage('success', 'Speaker accepted successfully');
        }, error(result): void {
          if (result.status === 403) {
            component.messagesService.addMessage('danger', 'Not authorized to accept speakers');
          }
        }
      });
  }

  async openRegisterSpeaker(speakerTemplate): Promise<void>{
    this.registerForm.patchValue({
      name : `${this.profile.firstName} ${this.profile.lastName}`,
      organization: ''
    });
    this.currentModal = this.modalService.open(speakerTemplate, {
      centered: true,
      ariaLabelledBy: 'modal-basic-title'
    });
  }

  async registerSpeaker(data: Speaker): Promise<void>{
    const component = this;
    this.http.post(config.serviceURLs.speaker + 'speaker/', data, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).subscribe({
      next(): void{
        component.resetSpeakers();
        component.registerForm.reset();
        component.currentModal.dismiss();
        component.messagesService.addMessage('success', 'You are registered as a speaker');
      },
      error(result): void{
        if (result.status === 403){
          component.currentModal.dismiss();
          component.messagesService.addMessage('danger', 'Not authorized to add yourself as a speaker');
        }
      }
    });
  }

  private async resetSpeakers(): Promise<void>{
    this.speakers = [];
    await this.ngOnInit();
  }
}
