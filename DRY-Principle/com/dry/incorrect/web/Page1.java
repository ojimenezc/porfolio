package com.dry.incorrect.web;

public class Page1 {

    public Page1(){
        this.loadPage();
    }
    
    public void loadPage() {
        if(isUserValid("123")){
            System.out.println("Page 1 loaded");
            //Do some code if user is valid
        }else{
            //Do something else if user is invalid
        }
    }

    public boolean isUserValid(String userId){
        //Do some user validation logic here
        return true;
    }
}
