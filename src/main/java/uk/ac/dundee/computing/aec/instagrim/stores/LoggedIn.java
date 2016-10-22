/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.dundee.computing.aec.instagrim.stores;

/**
 *
 * @author Administrator
 */
public class LoggedIn {
    boolean logedin=false;
    String Username=null;
    String fname=null;
    String lname=null;
    String email=null;
    boolean haveUserPic=false;
    String friend=null;
    public void LogedIn(){
        
    }
    public void setFirstname(String name){
        this.fname=name;
    }
    public void setFiend(String name){
        this.friend=name;
    }
    public void setLastname(String name){
        this.lname=name;
    }
    public void setEmail(String name){
        this.email=name;
    }
    public void setUsername(String name){
        this.Username=name;
    }
    public void sethaveUserPic(boolean flag){
        this.haveUserPic=flag;
    }
    public boolean gethaveUserPic(){
        return haveUserPic;
    }
    public String getUsername(){
        return Username;
    }
    public String getFirstname(){
        return fname;
    }
    public String getLastname(){
        return lname;
    }
    public String getEmail(){
        return email;
    }
    public void setLogedin(){
        logedin=true;
    }
    public void setLogedout(){
        logedin=false;
    }
    public boolean isFriend(String user){
        int a = friend.indexOf(user);
        if (a!=-1) {return true;}
        else {return false;}
    }
    public void addFriend(String user){
        this.friend=this.friend+user;
    }
    public void deleteFriend(String user){
        this.friend=this.friend.replace(user,"");
    }
    public void setLoginState(boolean logedin){
        this.logedin=logedin;
    }
    public boolean getlogedin(){
        return logedin;
    }
}
