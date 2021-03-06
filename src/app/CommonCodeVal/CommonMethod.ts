export class CommonMethod{

   public static authenticateUser():boolean{
    console.log("CommonMthod as - "+localStorage.getItem('tm@Ics#user'));   
    if(localStorage.getItem('tm@Ics#user')== null || localStorage.getItem('tm@Ics#user')== '' || 
       localStorage.getItem('tm@Ics#user')== 'null'){
            console.log("User is not Verified");
            return false;
    }else{
        return true;
    }
   }

   public static authenticateSenior():boolean{
    console.log("CommonMthod as - "+localStorage.getItem('tm@Ics#user') +' - '+ localStorage.getItem('tmRole'));   
    if(localStorage.getItem('tm@Ics#user')== null || localStorage.getItem('tm@Ics#user')== '' || 
       localStorage.getItem('tm@Ics#user')== 'null' || localStorage.getItem('tmRole') == '' 
       || localStorage.getItem('tmRole') == 'User'){
            console.log("User is not Verified");
            return false;
    }else{
        return true;
    }
   }

   public static clearLocalStorage(){
       localStorage.removeItem('tm@Ics#user');
       localStorage.removeItem('tmRole');
       localStorage.removeItem('tmManagerSelected');
      // localStorage.removeItem('tm@Ics#user'); 
       console.log(`Email - ${localStorage.getItem('tm@Ics#user')} , Role - ${localStorage.getItem('tmRole')}`);
   }

}