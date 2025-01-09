package com.jwt.security.model.starwars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Specie {

    private String name;
    private String classification;
    private String designation;
    private String averageHeight;
    private String averageLifespan;
    private String hairColors;
    private String skinColors;
    private String eyeColors;
    private String homeworld;
    private String language;
    private List<String> people;
    private List<String> films;
    private String url;
    private String created;
    private String edited;

}
