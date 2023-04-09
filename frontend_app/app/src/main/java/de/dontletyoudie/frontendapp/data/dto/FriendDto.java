package de.dontletyoudie.frontendapp.data.dto;

public class FriendDto {
    private String name;

    public FriendDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
