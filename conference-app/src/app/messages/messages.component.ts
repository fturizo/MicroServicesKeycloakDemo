import { Component, OnInit } from '@angular/core';
import {MessagesService} from '../messages.service';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.css']
})
export class MessagesComponent implements OnInit {

  constructor(public messageService: MessagesService) { }

  ngOnInit(): void {
  }

  closeMessage(id: number): void{
    this.messageService.removeMessage(id);
  }
}
