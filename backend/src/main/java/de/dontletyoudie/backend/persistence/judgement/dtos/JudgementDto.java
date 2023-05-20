package de.dontletyoudie.backend.persistence.judgement.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class JudgementDto {

    @JsonProperty
    private String judge;

    @JsonProperty
    private Long proofId;

    @JsonProperty
    private Boolean approved;

    @JsonProperty
    private LocalDateTime date;
}
