package com.quipux.playlist.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlaylistDTO {
    private String nombre;
    private String descripcion;
    private List<SongDTO> canciones;
}
