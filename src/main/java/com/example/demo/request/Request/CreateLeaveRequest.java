package com.example.demo.request.Request;

import com.example.demo.constant.OffType;
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
public class CreateLeaveRequest {
    @NotBlank(message = "Day start must not be blank or null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dateBegin;

    @NotBlank(message = "Day end must not be blank or null")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dateEnd;

    @NotBlank(message = "Message must not be blank or null")
    private String message;

    private OffType offType;

    private Reasons reasons;
    private Shift shiftStart;
    private Shift shiftEnd;

    @NotBlank(message = "Receiver email must not be blank or null")
    private String receiverEmail;


}
