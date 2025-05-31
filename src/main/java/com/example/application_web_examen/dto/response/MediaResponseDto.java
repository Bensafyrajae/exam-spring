package com.example.application_web_examen.dto.response;

import com.example.application_web_examen.enums.Type;
import com.example.application_web_examen.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDto {
    private Long id;
    private String mediaUrl;
    private String mediaId;
    private Type type;
    private User user;
}
