package com.codeoftheweb.Salvo.dtos;
import com.codeoftheweb.Salvo.models.Score;
import java.time.LocalDateTime;

public class ScoreDTO {

    private Long player;

    private Double score;

    private LocalDateTime finishDate;

    public ScoreDTO() {
    }

    public ScoreDTO(Score score){
        this.player = score.getPlayer().getId();
        this.score = score.getScore();
        this.finishDate = score.getFinishDate();
    }

    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }
}
