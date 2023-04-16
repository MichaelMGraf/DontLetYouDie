package de.dontletyoudie.frontendapp.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FriendListDto {
    @JsonProperty
    List<String> stringList;

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    @Override
    public String toString() {
        return "FriendListDto{" +
                "stringList=" + stringList +
                '}';
    }
}
