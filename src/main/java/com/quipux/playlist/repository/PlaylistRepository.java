package com.quipux.playlist.repository;

import com.quipux.playlist.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Playlist> findByNombre(String nombre);
}
