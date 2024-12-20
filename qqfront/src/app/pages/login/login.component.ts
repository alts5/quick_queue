import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {TimeComponent} from '../../components/time/time.component';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {Docs, DocService} from "../../services/doc.service";
import {map} from 'rxjs';
import {Cat, CatService} from "../../services/cat.service";

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
    @Input() attentionText: string = '* Обязательные  поля для заполнения формы'
    @Output() submitForm = new EventEmitter<any>();

    documentForm!: FormGroup;
    documentTypes = ['Студенческий билет', 'Паспорт', 'Водительское удостоверение'];
    applicantCategories = ['Студент', 'Пенсионер', 'Работающий'];
    docs: Docs[] = []
    cats: Cat[]=[]

    @Input() data!: { [key: string]: any };
    @Output() update = new EventEmitter<{ [key: string]: any }>()
    constructor(private doc: DocService,private cat: CatService) {
    }

    ngOnInit() {

        this.doc.getDocs().pipe(map((docs: Docs[]) => {  this.docs = docs}
        ) ).subscribe()
        // @ts-ignore
        this.cat.getCats().pipe(map((cats:Cat[])=>{this.cats=cats})).subscribe();
        this.documentForm = new FormGroup({
            fullName: new FormControl('', Validators.required),
            documentType: new FormControl('', Validators.required),
            documentNumber: new FormControl('', Validators.required),
            applicantCategory: new FormControl('', Validators.required),
            consent: new FormControl(false, Validators.requiredTrue)
        });
    }
    updateFields(updates: { [key: string]: any }) {
        this.update.emit(updates); // Передаем объект изменений
    }
    onSubmit() {
        if (this.documentForm.valid) {
            this.data['step']='select'
            this.data['cat']=this.documentForm.get("applicantCategory")?.value
            this.data['doc']=this.documentForm.get("documentType")?.value
            this.data['fullName']=this.documentForm.get("fullName")?.value
            this.data['documentData']=this.documentForm.get("documentNumber")?.value
            this.update.emit([{"cat":this.data["cat"]},{"doc":this.data["doc"]},{"fullName":this.data["fullName"]}
            ,{"documentData":this.data["documentData"]},{"step":this.data["select"]}]);
        }
    }
    toHome(){
        this.update.emit({"step":"root"});
    }
}
