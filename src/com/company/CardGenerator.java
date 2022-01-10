package com.company;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class CardGenerator {
    private Card[] allCards = new Card[12];


    public CardGenerator()  {

    }


    public Card[] shuffleCards() {
        Random n = new Random();
        int id = n.nextInt(999999999);
        Card card1 = new Card(id,"Fire",30);
        Card card2 = new Card(n.nextInt(999999999), "Water",10);
        Card card3 = new Card(n.nextInt(999999999),"Regular",9);
        Card card4 = new Card(n.nextInt(999999999),"FireSpell",15);
        Card card5 = new Card(n.nextInt(999999999), "WaterSpell",20);
        Card card6 = new Card(n.nextInt(999999999),"RegularSpell",10);
        Card card7 = new Card(n.nextInt(999999999),"Fire",30);
        Card card8 = new Card(n.nextInt(999999999), "Water",10);
        Card card9 = new Card(n.nextInt(999999999),"Regular",9);
        Card card10 = new Card(n.nextInt(999999999),"FireSpell",15);
        Card card11 = new Card(n.nextInt(999999999), "WaterSpell",20);
        Card card12 = new Card(n.nextInt(999999999),"RegularSpell",10);

        allCards[0]=card1;
        allCards[1]=card2;
        allCards[2]=card3;
        allCards[3]=card4;
        allCards[4]=card5;
        allCards[5]=card6;
        allCards[6]=card7;
        allCards[7]=card8;
        allCards[8]=card9;
        allCards[9]=card10;
        allCards[10]=card11;
        allCards[11]=card12;

        Card[] cardsToShuffle;
        Card[] shuffledCards = new Card[5];
        cardsToShuffle = allCards;
        Collections.shuffle(Arrays.asList(cardsToShuffle));

        for(int i=0; i<shuffledCards.length; i++) {
            shuffledCards[i] = cardsToShuffle[i];
        }

        return shuffledCards;


    }
}
