package com.company;

import java.util.ArrayList;
import java.util.Arrays;


public class Stack {
    private static final ArrayList<Card> stack = new ArrayList<>();
    private int counter = 0; // counter for cards

    public Stack(){
    }

    public String getStack(){
        StringBuilder strng = new StringBuilder("Stack: \n");
        for (Card stackCard : stack) {
            strng.append(stackCard.getID()).append(" -> ");
            strng.append(stackCard.getName()).append(" - ");
            strng.append(stackCard.getDamage()).append(" damage - ");
            strng.append(stackCard.getElementType()).append("\n");
        }
        return strng.toString();
    }

    public void addCard(int id, String name, float damage){
        Card card = new Card(id, name, damage);
        stack.add(card);
    }

}
