package com.codeoftheweb.Salvo.dtos;

import java.util.ArrayList;

public class HitDTO {


    private ArrayList self;

    private ArrayList opponent = new ArrayList();


    public HitDTO(){
        this.self =  new ArrayList();
        this.opponent = new ArrayList();
    }


    public ArrayList getSelf() {
        return self;
    }

    public void setSelf(ArrayList self) {
        this.self = self;
    }

    public ArrayList getOpponent() {
        return opponent;
    }

    public void setOpponent(ArrayList opponent) {
        this.opponent = opponent;
    }
}
