package com.codeoftheweb.Salvo.dtos;
import com.codeoftheweb.Salvo.models.Ship;
import com.codeoftheweb.Salvo.models.ShipType;

import java.util.List;

public class ShipDTO {

    private ShipType type;

    private List<String> locations;

    public ShipDTO() {
    }

    public ShipDTO(Ship ship){
        this.type = ship.getType();
        this.locations = ship.getShipLocations();
    }

    public ShipType getType() {
        return type;
    }

    public void setType(ShipType type) {
        this.type = type;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
