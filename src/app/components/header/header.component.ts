import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  userName:string= '';
  location:string= '';

  constructor() { }

  ngOnInit() {
    this.userName = localStorage.getItem('tmUserName');
    this.location = localStorage.getItem('tmLocation');
  } 

}
