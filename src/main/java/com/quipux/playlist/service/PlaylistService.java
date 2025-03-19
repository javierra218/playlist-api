package com.quipux.playlist.service;

import com.quipux.playlist.model.Playlist;
import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    Playlist createPlaylist(Playlist playlist);

    List<Playlist> getAllPlaylists();

    Optional<Playlist> getPlaylistByName(String name);

    void deletePlaylist(Long id);
}
