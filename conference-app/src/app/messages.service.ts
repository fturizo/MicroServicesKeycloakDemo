import { Injectable } from '@angular/core';

export class MessageDetail{
  constructor(public id: number,
              public type: 'success' | 'warning' | 'info' | 'danger',
              public contents: string) {
  }
}

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  public messages: Array<MessageDetail>;
  private counter: number;
  constructor() {
    this.messages = [];
    this.counter = 1;
  }

  addMessage(type: 'success' | 'warning' | 'info' | 'danger', contents: string): void {
    this.messages.push(new MessageDetail(this.counter++, type, contents));
  }

  removeMessage(id: number): void {
    const index = this.messages.findIndex((detail) => detail.id === id);
    if (index > -1){
      this.messages.splice(index, 1);
    }
  }

  clearMessages(): void{
    this.messages = [];
  }
}
