package com.example.application_web_examen.dto;


import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloudinaryResponse {

    public String publicId;
    public String url;


}
