package com.example.application_web_examen.dto.request;

import com.example.application_web_examen.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaRequestDto {
    private String mediaUrl;
    private String mediaId;
    private Type type;
    private Long productId;
    private Long userId;
}
