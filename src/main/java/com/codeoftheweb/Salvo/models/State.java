package com.codeoftheweb.Salvo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum State {

    @JsonProperty("PLACESHIPS")
    PLACESHIPS(0),
    @JsonProperty("WAITINGFOROPP")
    WAITINGFOROPP(1),
    @JsonProperty("PLAY")
    PLAY(2),
    @JsonProperty("WAIT")
    WAIT(3),
    @JsonProperty("WON")
    WON(4),
    @JsonProperty("TIE")
    TIE(5),
    @JsonProperty("LOST")
    LOST(6);

    private int value;
    State(int value){
        this.value = value;
    }

    private int getValue(){
        return value;
    }
}
