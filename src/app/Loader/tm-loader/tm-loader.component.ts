import { Component, OnInit , ChangeDetectorRef} from '@angular/core';
import { LoaderServiceService } from '../loader-service.service';

@Component({
  selector: 'app-tm-loader',
  templateUrl: './tm-loader.component.html',
  styleUrls: ['./tm-loader.component.css']
})
export class TmLoaderComponent implements OnInit {

  showSpinner = false;

  constructor(private spinnerService: LoaderServiceService , private cdRef: ChangeDetectorRef) {

  }

  ngOnInit() {
    this.init();
  }

  init() {

    this.spinnerService.getSpinnerObserver().subscribe((status) => {
      this.showSpinner = (status === 'start');
      this.cdRef.detectChanges();
    });
  }

}
