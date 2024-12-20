import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-lk',
  standalone: true,
  imports: [],
  templateUrl: './lk.component.html',
  styleUrl: './lk.component.scss'
})

export class LkComponent {
  @Input() data!: { [key: string]: any };
  @Output() update = new EventEmitter<{ [key: string]: any }>()
  public hash="dsfasdklf"
  constructor(){}
}
