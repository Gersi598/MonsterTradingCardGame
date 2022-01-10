package com.company;
import java.io.*;
import java.util.*;

public class Server  {

    private Battle battle;
    private final PostgreSQL db = new PostgreSQL();

    public Server() {

    }


    public void start(){


        int option;
        boolean logged = false;
        while(logged == false){
        option = mainMenu();

        if(option==1) {
            register();
        }else if(option==2){
            login();
            logged = true;
            break;
            }else{
            close();
        }

        }
        User user = new User();
        user.setUsername(login()) ;
        while(isUserLogged(user.getUsername())){

            int option2=loggedMenu(user);

            switch(option2){
                case 1 -> buyPackage(user);
                case 2 -> showStack(user);
                case 3 -> showDeck(user);
                case 4 -> trade(user);
                case 5 -> showDeckInfo(user);
                case 6 -> showStats(user);
                case 7 -> showScoreboard(user);
                case 8 -> battle(user);

                case 9 -> setStats(user);
                case 10 -> donate(user);
                case 11 -> logout(user);

                case 22 -> deleteAll();
            }

        }

    }

    public int mainMenu(){
        System.out.println("Please choose one of the options:\n");
        System.out.println("1   -> Create a new user");
        System.out.println("2   -> Login");
        System.out.println("3   -> Exit");
        Scanner sc = new Scanner(System.in);
        int option;
        option = sc.nextInt();
        return option;

    }

    public int loggedMenu(User user){
        System.out.println(" Welcome "+user.getUsername() );
        System.out.println("Please choose any of the options\n");
        System.out.println(" 1 -> Buy a Package\n" +
                " 2 -> Show Stack\n" +
                " 3 -> Show Deck\n" +
                " 4 -> Trade cards with a user\n" +
                " 5 -> Show plain Deck\n" +
                " 6 -> Show your Stats\n" +
                " 7 -> Show the Scoreboard\n" +
                " 8 -> Begin the battle\n" +
                " 9 -> Set your stats\n" +
                " 10 -> Donate 5 coins to someone\n" +
                " 11 -> Logout\n");

        Scanner sc = new Scanner(System.in);
        int option;
        option= sc.nextInt();
        return option;
    }

    public void register(){
        String username;
        String password;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username:\n");
        username = sc.nextLine();
        System.out.println("Choose a password\n");
        password = sc.nextLine();

        User user = new User(username,password);
        if (db.registerUser(user) == 1) {
            System.out.println("New user is created\n");
        } else {
            System.out.println("Username already exists\n");
        }

    }

    public String login(){
        String username;
        String password;
        Scanner sc = new Scanner(System.in);
        System.out.println("Login");
        System.out.println("Enter username:\n");
        username = sc.nextLine();

        System.out.println("Enter password\n");
        password = sc.nextLine();

        User user = new User(username,password);
        db.logInUser(user);
        if (db.logInUser(user) == 0) {
            System.out.println("Problems occurred during login!\n");
            return null;
        } else {
            System.out.println("Login successful.\n");
            return user.getUsername();
        }

    }

    public void close(){
        System.out.println("We are sorry to see you leave :(\nPlease be back soon!");
        System.exit(0);
    }

    public void logout(User user){
        db.logOutUser(user);
        if(db.logOutUser(user)==1){
        System.out.println("Logging out "+user.getUsername());}
        else{
            System.out.println("Problems occurred while logging out");
        }
    }



    private void battle(User user) {
        int id = db.getIdFromUsername(user.getUsername());
            if(isUserLogged(user.getUsername())){
                String opponent;
                Scanner sc = new Scanner(System.in);
                System.out.println("Choose your enemy:\nEvilWizard\nEvilElf\nEvilGoblin\nEvilDragon\n");
                opponent = sc.nextLine();
                User enemy = new User(opponent);
                int oid;
                oid=db.getIdFromUsername(enemy.getUsername());
                Deck deck = db.getDeck(id);
                Battle battle = new Battle();
                battle.addDeck(deck, user.getUsername());
               Deck deck2 = db.getDeck(oid);
               battle.addDeck(deck2,enemy.getUsername());
                if(battle.readyToBattle()){
                    String winner = battle.fight();
                    System.out.println(winner);
                }else{
                    System.out.println("Not ready to battle");
                }
            }else {
                System.out.println("User is not valid!");
            }
    }


    private void buyPackage(User user) {
        Package pack = new Package();
        pack.savePackage(pack.getPackageId());
        int id = pack.getPackageId();
            if (isUserLogged(user.getUsername())) {
                if (db.buyPackage(id, user.getUsername()) == 1) {
                    System.out.println("Package obtained");
                } else {
                    System.out.println("Cannot buy package");
                }
            } else {
                System.out.println("User is not valid");
            }


    }

    private void showStack(User user)  {
            int id = db.getIdFromUsername(user.getUsername());
            if(isUserLogged(user.getUsername())){
                String stack = db.getStack(id);
                System.out.println(stack);
            }else{
                System.out.println("User is not valid");
            }

    }

    public void deleteAll(){
        db.deleteAll();
    }

    private void donate(User user){
        System.out.println("Enter username of the user you want to donate 5 coins to:");
        Scanner sc = new Scanner(System.in);
        String uname = sc.nextLine();
        int result = db.donate(user,uname);
        if(result==1){
            System.out.println("Coins donated to "+uname);
        }else{
            System.out.println("Unable to donate coins");
        }
    }

    private void showDeck(User user)  {
                int id = db.getIdFromUsername(user.getUsername());
            if(isUserLogged(user.getUsername())){
                String deck = db.getDeckString(id, false);
                System.out.println(deck);
            }else{
                System.out.println("User is not valid");
            }

    }

    private void showDeckInfo(User user) {
        int id = db.getIdFromUsername(user.getUsername());
        if(isUserLogged(user.getUsername())){
                String deck = db.getDeckString(id, true);
                System.out.println(deck);
            }else{
                System.out.println("User is not valid");
            }

    }

    private void trade(User user)  {

        String[] usernames = new String[100];
        usernames=db.getUsernames();
        for(int i=0;i<10;i++){
            System.out.println(usernames[i]);
            if(usernames[i]==null){
                break;
            }
        }
        System.out.println("Choose the player to perform trading");
        Scanner sc = new Scanner(System.in);
        String player = sc.nextLine();
        int playerId=db.getIdFromUsername(player);
        String stack = db.getStack(playerId);
        System.out.println("Choosen players cards:");
        System.out.println(stack);
        int myid=db.getIdFromUsername(user.getUsername());
        String mystack = db.getStack(myid);
        System.out.println("Your cards:");
        System.out.println(mystack);
        System.out.println("Choose one of the selected player card id");
        int otherCardId = sc.nextInt();
        System.out.println("Choose the id of your card that you want to trade");
        int yourCardId= sc.nextInt();
        int result = db.tradeCards(myid,playerId,yourCardId,otherCardId);
        if(result==1){
            System.out.println("Trading performed successfully");
        }else{
            System.out.println("Could not perform trading!");
        }

    }

 private void setStats(User user) {
     System.out.println("Set your stats "+user.getUsername());
     System.out.println("New username:");
     Scanner sc = new Scanner(System.in);
     String uname = sc.nextLine();
     System.out.println("New bio status:");
     String bio = sc.nextLine();

     boolean updated;
     updated = db.setStats(user,uname,bio);
     if(updated){
         System.out.println("Stats set successfully");

     }else{
         System.out.println("Unable to set stats!");

     }

    }


    private void showStats(User user) {

            if(isUserLogged(user.getUsername())){
                String stats = db.getStats(user.getUsername());
                System.out.println(stats);
            }else{
                System.out.println("User is not valid");
            }

    }

    private void showScoreboard(User user) {

            if(isUserLogged(user.getUsername())){
                String stats = db.getScoreboard(user.getUsername());
                System.out.println(stats);
                System.out.println("\n");
            }else{
                System.out.println("User is not valid");
            }

    }

    public static void log(String msg) {
        File file = new File("log.txt");

        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);

            writer.write("logged: " + msg + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private boolean isUserLogged(String username) {
        if(db.isLogged(username)) {
            return true;

        } else {
            return false;
        }
    }}

