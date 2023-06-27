import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormBuilder} from '@angular/forms';
import {NgbDateParserFormatter, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {MessagesService} from '../messages.service';
import config from '../app.config';

interface Session{
  id: number;
  title: string;
  venue: string;
  date: Date;
  duration: string;
}

@Component({
  selector: 'app-sessions',
  templateUrl: './sessions.component.html',
  styleUrls: ['./sessions.component.css']
})
export class SessionsComponent implements OnInit {

  sessions: Array<Session>;
  private registeredSessionIds: Array<number>;
  currentModal: NgbModalRef;
  createForm;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private modalService: NgbModal,
              private messagesService: MessagesService,
              private dateParserFormater: NgbDateParserFormatter) {
    this.sessions = [];
    this.registeredSessionIds = [];
    this.createForm = formBuilder.group({
      title: '',
      venue: '',
      date: '',
      duration: ''
    });
  }

  async ngOnInit(): Promise<void> {
    this.http.get(config.serviceURLs.session + 'session/all')
      .subscribe((data: Array<Session>) => {
      [].push.apply(this.sessions, data);
      this.loadRegisteredIds();
    }, error => {
      if (error.status === 403){
        this.messagesService.addMessage('danger', 'Not authorized to view sessions');
      }
    });
  }

  private loadRegisteredIds(): void{
    this.http.get(config.serviceURLs.session + 'register/current')
      .subscribe((data: Array<Session>) => {
        this.registeredSessionIds = data.map(session => session.id);
    });
  }

  private resetSessions(): void{
    this.sessions = [];
    this.ngOnInit();
  }

  checkRegisteredSession(session: Session): boolean{
    return this.registeredSessionIds.indexOf(session.id) > -1;
  }

  async openCreateSession(sessionTemplate): Promise<void>{
    this.createForm.reset();
    this.currentModal = this.modalService.open(sessionTemplate, {
      centered: true,
      ariaLabelledBy: 'modal-basic-title'
    });
  }

  async createSession(data): Promise<void>{
    this.http.post(config.serviceURLs.session + 'session/', {
      title: data.title,
      venue: data.venue,
      date: this.dateParserFormater.format(data.date),
      duration: data.duration,
    }, {
      headers: {
        'Content-Type': 'application/json'
      }
    }).subscribe(() => {
      this.resetSessions();
      this.createForm.reset();
      this.currentModal.dismiss();
      this.messagesService.addMessage('success', 'Successfully added new session');
    }, error => {
      if (error.status === 403){
        this.currentModal.dismiss();
        this.messagesService.addMessage('danger', 'Not authorized to create new sessions');
      }
    });
  }

  async attendSession(session: Session): Promise<void>{
    this.http.post(config.serviceURLs.session + `register/${session.id}`, {}, {})
      .subscribe(() => {
        this.resetSessions();
        this.messagesService.addMessage('success', `You are attending the "${session.title}" session`);
      }, error => {
        if (error.status === 403){
          this.messagesService.addMessage('danger', 'Not authorized to attend sessions');
        }
      });
  }

  async deleteSession(session: Session): Promise<void>{
    this.http.delete(config.serviceURLs.session + `session/${session.id}`, {
      observe: 'response'
    }).subscribe(() => {
      this.resetSessions();
      this.messagesService.addMessage('success', 'Session deleted successfully');
    }, error => {
      if (error.status === 403){
        this.messagesService.addMessage('danger', 'Not authorized to delete sessions');
      }
    });
  }
}
