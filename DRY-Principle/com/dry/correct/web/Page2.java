package com.dry.correct.web;

import com.dry.correct.utilities.UsersValidation;

public class Page2 {

    public Page2(){
        this.loadPage();
    }

    public void loadPage() {
        UsersValidation userValidation = new UsersValidation();
        if(userValidation.isUserValid("123")){
            //Do some code if user is valid
            System.out.println("Page 2 loaded");
        }else{
            //Do something else if user is invalid
        }
    }
}