package com.dz.io.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto<T> {
    private String status;

    @Builder.Default
    private String message = "Success";

    private T body;
}
