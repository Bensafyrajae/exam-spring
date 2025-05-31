package com.example.application_web_examen.model;

import com.example.application_web_examen.enums.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mediaUrl;
    private String mediaId;
    private Type type;

    @JsonIgnore
    @OneToOne
    private User user;
}
