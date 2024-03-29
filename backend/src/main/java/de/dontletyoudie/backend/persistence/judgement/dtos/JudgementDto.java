package de.dontletyoudie.backend.persistence.judgement.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
