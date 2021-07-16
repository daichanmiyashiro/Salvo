package com.codeoftheweb.Salvo.dtos;
import com.codeoftheweb.Salvo.models.Player;

public class PlayerDTO {

    private Long id;

    private String email;

    public PlayerDTO() {
    }

    public PlayerDTO(Player player){
        this.id = player.getId();
        this.email = player.getUserName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
