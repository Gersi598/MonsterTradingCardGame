package com.company;
import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Random;



public class PostgreSQL {
    Connection connection;
    public PostgreSQL(){
        try {

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/mtcgdb", "test", "mtcg123");
        } catch (SQLException e) {
            System.out.println("Error connecting to database!");
            e.printStackTrace();
        }
    }

    public int registerUser(User user){
        try {
            PreparedStatement userExists = connection.prepareStatement( "SELECT * FROM users where username = ?" );
            userExists.setString(1, user.getUsername());
            ResultSet rs = userExists.executeQuery();
            if(rs.next()){
                return 0;
            }else{
               Random n = new Random();
                int uid = n.nextInt(13285,548543023);
                PreparedStatement st = connection.prepareStatement("INSERT INTO users (user_id, username, password, elo, coins, logged, bio) VALUES (?, ?, ?, ?, ?, ?, ?)");
               st.setInt(1, uid);
                st.setString(2, user.getUsername());
                st.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

                st.setInt(4, user.getEloRate());
                st.setInt(5, user.getCoins());
                st.setBoolean(6, user.isLogged());
                st.setString(7, user.getBio());
                st.executeUpdate();
                int sid = n.nextInt(13285,548543023);
                st = connection.prepareStatement("INSERT into score (score_id, user_id, victory, defeat, draws) values (?, ?, 0, 0, 0)");
                st.setInt(1, sid);
                st.setInt(2, getIdFromUsername(user.getUsername()));
                st.executeUpdate();
                st.close();
                return 1;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


    public boolean isLogged(String username){
        PreparedStatement userExst;
        try {
            userExst = connection.prepareStatement( "SELECT logged FROM users WHERE username = ?" );
            userExst.setString(1, username);
            ResultSet rs = userExst.executeQuery();
            if(rs.next()){
                return rs.getBoolean("logged");
            }else {
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public int logInUser(User user) {
        PreparedStatement stm;
        try {
            PreparedStatement userExst = connection.prepareStatement( "SELECT password FROM users where username = ?" );
            userExst.setString(1, user.getUsername());
            ResultSet rs = userExst.executeQuery();
            if(rs.next()){
                if(BCrypt.checkpw(user.getPassword(), rs.getString("password"))){
                    stm = connection.prepareStatement( "UPDATE users set logged = ? WHERE username = ?" );
                    stm.setBoolean(1, true);
                    stm.setString(2, user.getUsername());
                    int count = stm.executeUpdate();
                    stm.close();
                    if(count > 0) {
                        return 1;
                    }else{
                        return 0;
                    }
                }
            }else{
                return 0;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public int logOutUser(User user){
        PreparedStatement stm;
        try {
            stm = connection.prepareStatement("UPDATE users set logged = ? WHERE username = ?");
            stm.setBoolean(1, false);
            stm.setString(2, user.getUsername());
            int count = stm.executeUpdate();
            stm.close();
            if (count > 0) {
                return 1;
            } else {
                return 0;
            }

    }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;


    }

    public int donate(User user, String uname){
        PreparedStatement stm;
        try{
            stm = connection.prepareStatement("update users set coins= coins + 5 where username = ?");
            stm.setString(1,uname);
            stm.executeUpdate();
            stm = connection.prepareStatement("update users set coins = coins - 5 where username = ?");
            stm.setString(1,user.getUsername());
            stm.executeUpdate();
            stm.close();
            return 1;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public int getIdFromUsername(String username) {
        PreparedStatement userExst;
        try {
            userExst = connection.prepareStatement( "SELECT user_id FROM users where username = ?" );
            userExst.setString(1, username);
            ResultSet rs = userExst.executeQuery();
            if(rs.next()){
                return rs.getInt("user_id");
            }
            return 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public void insertCardToPackage(int id, Card card) {


        try {
            PreparedStatement st = connection.prepareStatement("INSERT INTO packages (package_id, card_id, name, damage) VALUES (?, ?, ?, ?)");
            st.setInt(1, id);
            st.setInt(2, card.getID());
            st.setString(3, card.getName());
            st.setDouble(4, card.getDamage());
            st.executeUpdate();
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public int buyPackage(int pid,String username){
        int id = getIdFromUsername(username);
        int coins = getCoinsFromUsername(username);
        if(pid != 0 && coins >= 5){
            Card[] cards = getCardsFromPackage(pid);
            deleteCardsFromPackage(pid);
            assert cards != null;
            if(saveCardsWithUser(cards, id) == 1 && decreaseCoinsFromUser(username) == 1){
                return 1;
            }
        }else{
            return 0;
        }
        return 0;
    }

    public int decreaseCoinsFromUser(String username) {
        PreparedStatement stm ;
        try {
            stm = connection.prepareStatement( "UPDATE users set coins = coins - 5 WHERE username = ?" );
            stm.setString(1, username);
            if(stm.executeUpdate() > 0) {
                stm.executeUpdate();
                stm.close();
                return 1;
            }else{
                stm.close();
                return 0;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  0;

    }

    private int getCoinsFromUsername(String username) {
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement( "SELECT coins FROM users where username = ?");
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
            {
                return rs.getInt("coins");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    private Card[] getCardsFromPackage(int id){
        PreparedStatement stmt;
        Card[] cards = new Card[5];
        try {
            stmt = connection.prepareStatement( "SELECT * FROM packages where package_id = ?");
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while (rs.next())
            {
                cards[count] = new Card(rs.getInt("card_id"), rs.getString("name"), rs.getFloat("damage"));
                count++;
            }
            return cards;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String[] getUsernames(){
        PreparedStatement stmt;
        String[] str = new String[10];
        try{
            stmt = connection.prepareStatement( "SELECT username from users");
            ResultSet rs = stmt.executeQuery();
            int count=0;
            while (rs.next())
            {
                str[count] =( rs.getString("username"));
                count++;
            }
            return str;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    public int deleteCardsFromPackage(int id){
        PreparedStatement stmt;
        try {
            stmt = connection.prepareStatement( "Delete FROM packages where package_id = ?");
            stmt.setInt(1,id);
            stmt.executeUpdate();
            stmt.close();
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    private int saveCardsWithUser(Card[] cards, int userId){
        try {
            for(Card card: cards){
                PreparedStatement st = connection.prepareStatement("INSERT INTO cards (card_id, user_id, name, damage) VALUES (?, ?, ?, ?)");
                st.setInt(1, card.getID());
                st.setInt(2, userId);
                //st.setInt(3, 0);
                st.setString(3, card.getName());

                st.setFloat(4, card.getDamage());
                st.executeUpdate();


                st.close();
            }
            return 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public int tradeCards(int userId1, int userId2, int myCardId, int otherCardId){
        try{
            PreparedStatement st = connection.prepareStatement("update cards set user_id = (?) where card_id = (?)");
            st.setInt(1,userId1);
            st.setInt(2,otherCardId);
            st.executeUpdate();
            st = connection.prepareStatement("update cards set user_id = (?) where card_id = (?)");
            st.setInt(1,userId2);
            st.setInt(2,myCardId);
            st.close();
            return 1;
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public String getStack(int uid){
        try {
            PreparedStatement st = connection.prepareStatement("select * from cards where user_id in (?)");
            st.setInt(1, uid);
            ResultSet rs = st.executeQuery();
            Stack stack = new Stack();
            while (rs.next())
            {
                stack.addCard(rs.getInt("card_id"), rs.getString("name"), rs.getFloat("damage"));

            }
            return stack.getStack();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public String getDeckString(int userId, boolean plain) {
        try {
            PreparedStatement st = connection.prepareStatement("select * from cards where user_id in (?);");
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            Deck deck = new Deck();
            while(rs.next())
            {
                Card card = new Card(rs.getInt("card_id"), rs.getString("name"), rs.getFloat("damage"));
                deck.append(card);
            }
            if(deck.showDeck() != null){
                if(plain){
                    return deck.showDeckInfo();
                }
                return deck.showDeck();
            }else{
                return "Deck is empty";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "An unexpected problem has occurred!";
    }

    public Deck getDeck(int userId) {
        try {
            PreparedStatement st = connection.prepareStatement("select * from cards where user_id in (?);");
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            Deck deck = new Deck();
            while(rs.next())
            {
                Card card = new Card(rs.getInt("card_id"), rs.getString("name"), rs.getFloat("damage"));
                deck.append(card);
            }

            return deck;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public String getStats(String username) {
        try {
            PreparedStatement st = connection.prepareStatement("select * from users as u join score as s on u.user_id = s.user_id where username = ?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            User user = null;
            while(rs.next())
            {
                user = new User(rs.getString("username"));
                user.setEloRate(rs.getInt("elo"));
                user.setCoins(rs.getInt("coins"));
                user.setBio(rs.getString("bio"));
            }
            assert user != null;
            return user.getStats();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getScoreboard(String username) {
        try {
            PreparedStatement st = connection.prepareStatement("select victory, defeat, draws from users as u join score as s on u.user_id = s.user_id where username = ?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            StringBuilder scoreBoard = new StringBuilder("Scoreboard:\n");
            while(rs.next())
            {
                scoreBoard.append("\tVictories: ").append(rs.getString("victory"));
                scoreBoard.append("\n\tDefeat: ").append(rs.getString("defeat"));
                scoreBoard.append("\n\tDraws: ").append(rs.getString("draws"));
            }
            return scoreBoard.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public boolean setStats(User user,String uname, String Bio) {


        int id = getIdFromUsername(user.getUsername());
        try {
            PreparedStatement st = connection.prepareStatement("update users set username = ?, bio = ?  where user_id = ?");
            st.setString(1, uname);
            st.setString(2, Bio);
            st.setInt(3, id);
            int count = st.executeUpdate();
            if(count > 0){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void deleteAll(){
        try{
            PreparedStatement st1 = connection.prepareStatement("DELETE  from cards");
            st1.executeUpdate();
            PreparedStatement st2 = connection.prepareStatement("DELETE  from users");
            st2.executeUpdate();
            PreparedStatement st3 = connection.prepareStatement("DELETE  from packages");
            st3.executeUpdate();
            PreparedStatement st4 = connection.prepareStatement("DELETE  from score");
            st4.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void updateScore(String username1, String username2, int result) {
        try {
            PreparedStatement st;
            if(result == 1){
                st = connection.prepareStatement("update score set victory = victory+1 where user_id = ?");
                st.setInt(1, getIdFromUsername(username1));
                st.executeUpdate();
                st = connection.prepareStatement("update users set elo = elo+3 where username = ?");
                st.setString(1, username1);
                st.executeUpdate();
                st = connection.prepareStatement("update score set defeat = defeat+1 where user_id = ?");
                st.setInt(1, getIdFromUsername(username2));
                st.executeUpdate();
                st = connection.prepareStatement("update users set elo = elo-5 where username = ?");
                st.setString(1, username2);
                st.executeUpdate();
            }else if(result == 2){
                st = connection.prepareStatement("update score set victory = victory+1 where user_id = ?");
                st.setInt(1, getIdFromUsername(username2));
                st.executeUpdate();
                st = connection.prepareStatement("update users set elo = elo+3 where user_id = ?");
                st.setInt(1, getIdFromUsername(username2));
                st.executeUpdate();
                st = connection.prepareStatement("update score set defeat = defeat+1 where user_id = ?");
                st.setInt(1, getIdFromUsername(username1));
                st.executeUpdate();
                st = connection.prepareStatement("update users set elo = elo-5 where user_id = ?");
                st.setInt(1, getIdFromUsername(username1));
                st.executeUpdate();
            }else{
                st = connection.prepareStatement("update score set draws = draws+1 where user_id = ?");
                st.setInt(1, getIdFromUsername(username2));
                st.executeUpdate();
                st = connection.prepareStatement("update score set draws = draws+1 where user_id = ?");
                st.setInt(1, getIdFromUsername(username1));
                st.executeUpdate();
            }
            st.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}



