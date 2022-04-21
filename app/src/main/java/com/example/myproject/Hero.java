package com.example.myproject;

public class Hero {
    private int fatherRel;
    private int popularity; //Некоторые негативные ивенты будут запускаться допом
    private int money;
    private int fightSkill;

    public Hero(int fatherRel, int popularity, int money, int fightSkill){
        this.fatherRel = fatherRel;
        this.popularity = popularity;
        this.money = money;
        this.fightSkill = fightSkill;
    }

    public int getFightSkill() {
        return fightSkill;
    }

    public int getFatherRel() {
        return fatherRel;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getMoney() {
        return money;
    }

    public void setFatherRel(int fatherRel) {
        this.fatherRel = fatherRel;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setFightSkill(int fightSkill) {
        this.fightSkill = fightSkill;
    }

    public void boostMoney(int money){
        this.money += money;
    }

    public void boostFatherRel(int fatherRel){
        this.fatherRel += fatherRel;
    }

    public void boosPopularity(int popularity){
        this.popularity += popularity;
    }

    public void boostFightSkill(int fightSkill){
        this.fightSkill += fightSkill;
    }
}
