export class address {
    address: string;
    city : string;
    state: string;
    zipCode: string;
    country: string

    constructor( ){
        this.address= '';
        this.city= '';
        this.state= '';
        this.zipCode= '';
        this.country= '';
    }
}

export class IAllEmployee {
    empId: number=0;
    manager: string='';
    firstName: string='';
    lastName: string;
    emailId: string;
    role: string;
    address: address;
    createDate : string;
    password : string;
    project : string

    get firstName1(): string {
        return this.firstName;
    }
    set firstName1(value: string) {
        this.firstName = value;
    }

}
    // constructor(userResponse:any) {
    //     this.empId = userResponse.empId;
    //     this.manager = userResponse.manager;
    //     this.firstName = userResponse.firstName;
    //     this.lastName = userResponse.lastName;
    //     this.emailId = userResponse.emailId;
    //     this.role = userResponse.role;
    //     this.address = userResponse.Address;
    //     this.createDate = userResponse.createDate;
    //     this.password = userResponse.password;
    //     this.project = userResponse.project;
    
        // this.address.address= userResponse.address.address;
        // this.address.city= userResponse.address.city;
        // this.address.state= userResponse.address.state;
        // this.address.zipCode= userResponse.address.zipCode;
        // this.address.country= userResponse.address.country;}
    


    


