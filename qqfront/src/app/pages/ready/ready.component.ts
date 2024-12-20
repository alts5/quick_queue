import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {TimeComponent} from '../../components/time/time.component';
import {QRCodeModule} from 'angularx-qrcode';
import {HashResponse, SingleregService} from "../../services/singlereg.service";
import {map} from "rxjs/operators";

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
    constructor(private singleReg:SingleregService) {
    }

    ngOnInit() {
    if(this.data['mode']=='single'){
        this.singleReg.singleReg().pipe(map((hash:HashResponse)=>{
            this.hash=hash.hash
            this.link='http://0.0.0.0:4200/lk?hash='+hash.hash
        })).subscribe()
    }
    }
    refresh() {
        this.data["step"]="root"
        this.update.emit({"step":this.data["step"]});
    }


}

