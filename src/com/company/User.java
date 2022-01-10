package com.company;
import org.json.JSONObject;

public class User {

    private String username;
    private String password;
    private String bio;
    private int coins;

    private boolean logged;
    private int eloRate;

    public User(){
        coins = 20;
        logged = false;
        bio = "Anonymous";
        eloRate = 100;
    }

    public User(String username){
        this.username = username;
    }

    public User(String username, String password){
        coins = 40;
        eloRate = 100;
        this.username = username;
        this.password = password;
        bio="HelloWorld. My name is "+username;

    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLogged() {
        return logged;
    }

    public int getCoins(){
        return coins;
    }

    public String getBio(){ return bio;}
    public void setBio(String bio){this.bio = bio;}

    public String setUsername(String username){
       return this.username = username;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getEloRate(){ return eloRate; }

    public void setEloRate(int eloRate){ this.eloRate=eloRate; }



    public String getStats(){
        return "User stats:\n" + "\n\tUserame: " + username +
                "\n\tElo Rating: " + eloRate +
                "\n\tCoins: " + coins +
                "\n\tBio: " + bio ;

    }




}
