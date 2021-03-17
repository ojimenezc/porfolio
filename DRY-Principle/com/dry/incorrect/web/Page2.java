package com.dry.incorrect.web;

public class Page2 {

    public Page2(){
        this.loadPage();
    }

    public void loadPage() {
        if(isUserValid("123")){
            //Do some code if user is valid
            System.out.println("Page 2 loaded");
        }else{
            //Do something else if user is invalid
        }
    }

    public boolean isUserValid(String userId){
        //Do some user validation logic here
        return true;
    }
}