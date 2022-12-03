package de.dontletyoudie.backend.persistence.proof.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.awt.*;
import java.time.ZonedDateTime;

@Getter
@ToString
public class ProofAddDto extends ProofAddDtoWithoutPicture {
    byte[] image;

    public ProofAddDto(String username, byte[] image, ZonedDateTime creationDate, String category, String comment) {
        super(username, creationDate, category, comment);
        this.image = image;
    }
}
