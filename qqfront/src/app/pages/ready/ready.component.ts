import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {TimeComponent} from '../../components/time/time.component';
import {QRCodeModule} from 'angularx-qrcode';
import {HashResponse, SingleregService} from "../../services/singlereg.service";
import {map} from "rxjs/operators";
import {regService, RegService} from "../../services/reg.service";

@Component({
    selector: 'app-ready',
    standalone: true,
    imports: [
        TimeComponent,
        RouterOutlet,
        QRCodeModule
    ],
    templateUrl: './ready.component.html',
    styleUrl: './ready.component.scss'
})

export class ReadyComponent {
    @Input() data!: { [key: string]: any };
    @Output() update = new EventEmitter<{ [key: string]: any }>()
    public hash :string=""
    public link:string=""
    constructor(private singleReg:SingleregService, private  reg:RegService) {
    }

    ngOnInit() {
    if(this.data['mode']=='single'){
        this.singleReg.singleReg().pipe(map((hash:HashResponse)=>{
            this.hash=hash.hash
            //http://localhost:8080/121ticket?hash=
            this.link='http://0.0.0.0:4200/lk?hash='+hash.hash
        })).subscribe()
    }else if(this.data['mode']=='multi'){
        this.reg.Reg(this.data['cat'],this.data['doc'],this.data['documentData'],this.data['fullName']).pipe(map((hash:regService)=>{
            this.hash=hash.hash
            this.link='http://0.0.0.0:4200/lk?hash='+hash.hash
        })).subscribe()
    }
    }
    refresh() {
        this.data["step"]="root"
        this.update.emit({"step":"root"});
    }


}

