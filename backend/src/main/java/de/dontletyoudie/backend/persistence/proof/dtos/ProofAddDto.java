package de.dontletyoudie.backend.persistence.proof.dtos;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class ProofAddDto extends ProofAddDtoWithoutPicture {
    byte[] image;

    public ProofAddDto(String username, byte[] image, ZonedDateTime creationDate, String categoryName, String comment) {
        super(username, creationDate, categoryName, comment);
        this.image = image;
    }
}
