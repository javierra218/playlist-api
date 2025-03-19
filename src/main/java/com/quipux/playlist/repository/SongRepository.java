package com.quipux.playlist.repository;

import com.quipux.playlist.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtista(String artista);
}
