package org.technoready.researcherdataintegration.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class Error {
    private int code;
    private String message;
    private String error;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String path;

    public Error(int code, String message, String error, LocalDateTime timestamp, String path) {
        this.code = code;
        this.message = message;
        this.error = error;
        this.timestamp = timestamp;
        this.path = path;
    }

}
