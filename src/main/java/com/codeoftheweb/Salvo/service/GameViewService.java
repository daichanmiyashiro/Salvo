package com.codeoftheweb.Salvo.service;
import com.codeoftheweb.Salvo.dtos.GameViewDTO;
import com.codeoftheweb.Salvo.models.Game;
import com.codeoftheweb.Salvo.models.GamePlayer;

public interface GameViewService {

    public GameViewDTO makeGameViewDTO(Game game, GamePlayer gamePlayer);

}
