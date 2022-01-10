package com.company;

import java.util.Random;


public class Package {

    private Card[] packageCards = new Card[5];
    private int id;

    public Package() {
        CardGenerator pack = new CardGenerator();
        packageCards=pack.shuffleCards();
        Random n = new Random();
        id=n.nextInt(1,999999999);

    }

    public int getPackageId(){
        return id;
    }


    public void savePackage(int id){
        PostgreSQL db = new PostgreSQL();
        for(int i=0; i < 5; i++) {
            db.insertCardToPackage(id, packageCards[i]);
        }
    }


    static String showCards(Card[] deckCards) {
        StringBuilder str = new StringBuilder("Deck: \n");
        for (Card deckCard : deckCards) {
            str.append(deckCard.getID()).append(" : ");
            str.append(deckCard.getName()).append(" - ");
            str.append(deckCard.getDamage()).append(" damage - ");
            str.append(deckCard.getElementType()).append("\n");
        }
        return str.toString();
    }


    public Card[] getPackage(){
        return packageCards;
    }
}
