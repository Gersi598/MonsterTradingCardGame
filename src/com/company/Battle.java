package com.company;
import com.company.Enum_.TYPE;
import com.company.Enum_.ELEMENT;
import java.util.Random;
import java.util.Arrays;


public class Battle {

    private String username1;
    private String username2;
    private final PostgreSQL db = new PostgreSQL();
    private Deck deck1;
    private Deck deck2;
    int[] takenCards1 = new int[4];
    int[] takenCards2 = new int[4];

    public Battle(){
        Arrays.fill(takenCards1, 0);
        Arrays.fill(takenCards2, 0);
    }

    public void addDeck(Deck deck, String username){
        if(deck1 == null){
            deck1 = deck;
            username1 = username;
        }else{
            if(deck2 == null){
                deck2 = deck;
                username2 = username;
            }
        }
    }

    public boolean readyToBattle(){
        return deck1 != null && deck2 != null;
    }

    public String fight(){
        Random rand = new Random();

        Server.log(username1 + " against " + username2 + "\n");

        for(int i=0; i<100; i++){
            Server.log("Round " + (i+1) + "");
            int randNum;
            int card1, card2;

            while(true){
                randNum = rand.nextInt(4) + 1;
                if(!isTaken(randNum, 1)){
                    card1 = randNum;
                    break;
                }
            }
            while(true){
                randNum = rand.nextInt(4) + 1;
                if(!isTaken(randNum, 2)){
                    card2 = randNum;
                    break;
                }
            }
            fightCards(deck1.getCard(card1), card1, deck2.getCard(card2), card2);
            if(isOver() || i==99){
                return checkWinner();
            }

        }
        return null;
    }

    public String checkWinner(){
        int cardsDown1, cardsDown2;
        cardsDown1 = getTakenCards(1);
        cardsDown2 = getTakenCards(2);
        clean();
        if(cardsDown1 > cardsDown2){
            db.updateScore(username1, username2, 1);
            return username1 + " is the winner\n";
        }else if(cardsDown2 > cardsDown1){
            db.updateScore(username1, username2, 2);
            return username2 + " is the winner\n";
        }else{
            db.updateScore(username1, username2, 0);
            return "This battle was a draw\n";
        }
    }

    private void clean() {
        Arrays.fill(takenCards1, 0);
        Arrays.fill(takenCards2, 0);
        deck1 = null;
        deck2 = null;
    }

    public void fightCards(Card card1, int card1Num, Card card2, int card2Num) {
        int winner;
        Server.log(username1 + " plays his card: " + card1.getName());
        Server.log(username2 + " plays his card: " + card2.getName());
        if(card1.getCardType() == card2.getCardType()){
            if(card1.getCardType() == TYPE.MONSTER){
                Server.log("Both cards are monster cards.");
                winner = compareCards(card1,card1Num, card2, card2Num);
            }else{
                Server.log("Both cards are spell cards.");
                winner = getWinner(card1, card1Num, card2, card2Num);
            }
        }else{
            Server.log("Cards types are different.");
            Server.log(card1.getName() + " type is " + card1.getElementType());
            Server.log(card2.getName() + " type is " + card2.getElementType());
            winner = getWinner(card1, card1Num, card2, card2Num);
        }

        switch (winner) {
            case 0 -> Server.log("This round is a draw\n");
            case 1 -> Server.log(username1 + " has won this round.\n");
            case 2 -> Server.log(username2 + " has won this round.\n");
        }
    }

    private int getWinner(Card card1, int card1Num, Card card2, int card2Num) {
        int winner;

        if(card1.getElement() == ELEMENT.WATER && card2.getElement() == ELEMENT.FIRE){
            card1.doubleDamage();

            card2.halfDamage();
            winner = compareCards(card1,card1Num, card2, card2Num);
        }else if(card2.getElement() == ELEMENT.WATER && card1.getElement() == ELEMENT.FIRE){
            card2.doubleDamage();

            card1.halfDamage();
            winner = compareCards(card1,card1Num, card2, card2Num);
        }else if(card1.getElement() == ELEMENT.FIRE && card1.getElement() == ELEMENT.NORMAL){
            card1.doubleDamage();
            card2.halfDamage();
            winner = compareCards(card1,card1Num, card2, card2Num);
        }else if(card2.getElement() == ELEMENT.NORMAL && card1.getElement() == ELEMENT.FIRE) {
            card2.doubleDamage();
            card1.halfDamage();
            winner = compareCards(card1, card1Num, card2, card2Num);
        }
        else if(card1.getElement() == ELEMENT.NORMAL && card1.getElement() == ELEMENT.WATER){
            card1.doubleDamage();
            card2.halfDamage();
            winner = compareCards(card1,card1Num, card2, card2Num);
        }else if(card2.getElement() == ELEMENT.WATER && card1.getElement() == ELEMENT.NORMAL) {
            card2.doubleDamage();
            card1.halfDamage();
            winner = compareCards(card1, card1Num, card2, card2Num);
        }else{
            winner = compareCards(card1,card1Num, card2, card2Num);
        }
        return winner;
    }

    public int compareCards(Card card1, int card1Num, Card card2, int card2Num){
        if(card1.getDamage() > card2.getDamage()){
            occupyInt(card2Num, 2);
            return 1;
        }else if(card1.getDamage() < card2.getDamage()){
            occupyInt(card1Num, 1);
            return 2;
        }else{
            return 0;
        }
    }

    public void occupyInt(int number, int player){
        if(player == 1){
            for( int i=0; i<4; i++){
                if(takenCards1[i] == 0){
                    takenCards1[i] = number;
                    break;
                }
            }
        }else{
            for( int i=0; i<4; i++){
                if(takenCards2[i] == 0){
                    takenCards2[i] = number;
                    break;
                }
            }
        }
    }

    public boolean isTaken(int number, int player){
        if(player == 1){
            for( int i=0; i<4; i++){
                if(takenCards1[i] == number){
                    return true;
                }
            }
        }else{
            for( int i=0; i<4; i++){
                if(takenCards2[i] == number){
                    return true;
                }
            }
        }
        return false;
    }

    private int getTakenCards(int player){
        int count = 0;
        if(player == 1){
            for(int i = 0; i<4; i++){
                if(takenCards1[i] != 0){
                    count += 1;
                }
            }
        }else{
            for(int i = 0; i<4; i++){
                if(takenCards2[i] != 0){
                    count += 1;
                }
            }
        }
        return count;
    }


    public boolean isOver(){
        for(int i=0; i<4; i++){
            if(takenCards1[i] == 0){
                return false;
            }
        }
        return true;
    }



}
