package com.example.demo.request.Request;

import com.example.demo.constant.LateOrSoon;
import com.example.demo.constant.Reasons;
import com.example.demo.constant.Shift;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSoonLateRequest {
    @NotBlank(message = "Write the time you need")
    private String timeLateOrSoon;

    @NotBlank(message = "Message must not be blank or null")
    private String message;

    @NotBlank(message = "timeBegin must not be blank")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dayRequest;

    private Shift shift;

    private Reasons reasons;

    private LateOrSoon lateOrSoon;

    @NotBlank(message = "Receiver email must not be blank or null")
    private String receiverEmail;
}
