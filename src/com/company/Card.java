package com.company;
import com.company.Enum_.ELEMENT;
import com.company.Enum_.TYPE;


public class Card {
    private String name;
    private int id;
    private TYPE cardType;
    private ELEMENT elementType;
    private float damage;

    public Card() {
        this.name = "Default";
        this.cardType = TYPE.NONE;
        this.elementType = ELEMENT.NONE;
        this.damage = 10;
    }

    public Card(int id, String name, float damage) {
        this.name = name;
        this.damage = damage;
        this.id=id;
        configuration( name);
    }

    public void configuration( String name){
        if(name.contains("Fire") || name.contains("Water") || name.contains("Regular")){
            if(name.contains("Spell")){
                cardType = TYPE.SPELL;
                if(name.contains("Water")){
                    elementType = ELEMENT.WATER;
                }else if(name.contains("Fire")){
                    elementType = ELEMENT.FIRE;
                }else{
                    elementType = ELEMENT.NORMAL;
                }
            }else{
                elementType = ELEMENT.NORMAL;
                cardType = TYPE.MONSTER;
            }
        }else{
           /* if(name.contains("Monster")){
                if(name.contains("Water")){
                    elementType = ELEMENT.WATER;
                }
                else if(name.contains("Fire")){
                    elementType = ELEMENT.FIRE;
                }else{
                    elementType = ELEMENT.NORMAL;
                }

            } */
            elementType = ELEMENT.NONE;
            cardType = TYPE.MONSTER;
        }

    }

    public String getName() {
        return name;
    }

    public int getID(){
        return id;
    }

    public float getDamage() {
        return damage;
    }

    public void doubleDamage() {
        damage = damage * 2;
    }

    public void halfDamage() {
        damage = damage / 2;
    }


    public String getElementType(){
        return elementType.toString();
    }

    public ELEMENT getElement(){
        return elementType;
    }

    public String isMonster(){
        return cardType.toString();
    }

    public TYPE getCardType() {
        return cardType;
    }
}
