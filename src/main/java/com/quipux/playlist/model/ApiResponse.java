package com.quipux.playlist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String mensaje;
    private T data;
    private String status;

    public ApiResponse(String mensaje, String status) {
        this.mensaje = mensaje;
        this.data = null;
        this.status = status;
    }
}
