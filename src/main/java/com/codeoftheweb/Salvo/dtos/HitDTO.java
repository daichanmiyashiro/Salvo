package com.codeoftheweb.Salvo.dtos;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HitDTO {

    private List<GameHistoryDTO> self;

    private List<GameHistoryDTO> opponent;

    public HitDTO() {
    }

    public HitDTO(Map<String , Object> map){
        List<Map<String , Object>> selfList = (List<Map<String , Object>>) map.get("self");
        List<Map<String , Object>> opponentList = (List<Map<String , Object>>) map.get("opponent");
        this.self = selfList.stream().map(self -> new GameHistoryDTO(self)).collect(Collectors.toList());
        this.opponent =opponentList.stream().map(opponent -> new GameHistoryDTO(opponent)).collect(Collectors.toList());
    }

    public List<GameHistoryDTO> getSelf() {
        return self;
    }

    public void setSelf(List<GameHistoryDTO> self) {
        this.self = self;
    }

    public List<GameHistoryDTO> getOpponent() {
        return opponent;
    }

    public void setOpponent(List<GameHistoryDTO> opponent) {
        this.opponent = opponent;
    }
}
