package com.example.notasapp.listeners;

import com.example.notasapp.entidades.Nota;

public interface NotasListener {
    void onNoteClicked(Nota nota, int position);
}
