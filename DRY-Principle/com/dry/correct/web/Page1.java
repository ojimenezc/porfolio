package com.dry.correct.web;

import com.dry.correct.utilities.UsersValidation;

public class Page1 {

    public Page1(){
        this.loadPage();
    }
    
    public void loadPage() {
        UsersValidation userValidation = new UsersValidation();
        if(userValidation.isUserValid("123")){
            System.out.println("Page 1 loaded");
            //Do some code if user is valid
        }else{
            //Do something else if user is invalid
        }
    }
}
