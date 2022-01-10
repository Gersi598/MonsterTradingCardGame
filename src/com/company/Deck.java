package com.company;
import com.company.Card;
import java.util.Arrays;
import com.company.Package;

public class Deck {

    private final Card[] deck = new Card[4];


    public Deck(){
        Arrays.fill(deck, null);
    }


    public int checkDeck(){
        for (Card deckCard : deck) {
            if (deckCard == null) {
                    return 1;
            }
        }
        return 0;
    }

    public void append(Card card){
        int cnt = 0;
        for( ; cnt< deck.length; cnt++){
            if( deck[cnt] == null){
                deck[cnt] = card;
                break;
        }
    }}


    public Card getCard(int index){
        return deck[index-1];}

    public String showDeckInfo(){
        StringBuilder deckstr = new StringBuilder();
        deckstr.append("Deck:\n");
        if(checkDeck() == 0){
            for( int i=0; i<4; i++){
                deckstr.append(deck[i].getName()).append(" type is ");
                deckstr.append(deck[i].getElementType()).append("\n");
            }
            return deckstr.toString();
        }else{
            System.out.println("Unable to show! Deck not created!");
            return null;
        }
    }

    public String showDeck(){
        if(checkDeck() == 0){
            return Package.showCards(deck);

        }else{
            System.out.println("Unable to show! Deck not created!");
            return null;
        }
    }
}
