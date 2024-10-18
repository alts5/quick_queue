import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {TimeComponent} from '../../components/time/time.component';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterOutlet,
    TimeComponent,
    ReactiveFormsModule,
    NgForOf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  @Input() title: string = 'Пожалуйста, предоставьте документы, чтобы получить услугу';
  @Input() footerText: string = '<настраиваемая_надпись_снизу>';
  @Output() submitForm = new EventEmitter<any>();

  documentForm!: FormGroup;
  documentTypes = ['Студенческий билет', 'Паспорт', 'Водительское удостоверение'];
  applicantCategories = ['Студент', 'Пенсионер', 'Работающий'];

  constructor() {
  }

  ngOnInit() {
    this.documentForm = new FormGroup({
      fullName: new FormControl('', Validators.required),
      documentType: new FormControl('', Validators.required),
      documentNumber: new FormControl('', Validators.required),
      applicantCategory: new FormControl('', Validators.required),
      consent: new FormControl(false, Validators.requiredTrue)
    });
  }

  onSubmit() {
    if (this.documentForm.valid) {
      this.submitForm.emit(this.documentForm.value);
    }
  }
}
