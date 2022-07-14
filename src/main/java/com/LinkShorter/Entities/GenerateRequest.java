package com.LinkShorter.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateRequest {
    @NotNull(message = "field link cannot be null")
    @NotBlank(message = "field link cannot be empty")
    @URL(message = "field link must be a valid URL address")
    private String link;
}