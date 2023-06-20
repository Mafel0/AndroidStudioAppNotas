package com.example.notasapp.adapters;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notasapp.R;
import com.example.notasapp.entidades.Nota;
import com.example.notasapp.listeners.NotasListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.NotaViewHolder>{

    private List<Nota> notas;
    private NotasListener notasListener;
    private Timer timer;
    private List<Nota> notaSource;

    public NotasAdapter(List<Nota> notas, NotasListener notasListener) {
        this.notas = notas;
        this.notasListener = notasListener;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotaViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_nota,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, final int position) {
        holder.setNota(notas.get(position));
        holder.layoutNota.setOnClickListener(v -> notasListener.onNoteClicked(notas.get(position), position));
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo, textSubtitulo, textDateTime;
        LinearLayout layoutNota;
        RoundedImageView imageNota;

        NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTitulo);
            textSubtitulo = itemView.findViewById(R.id.textSubtitulo);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNota = itemView.findViewById(R.id.layoutNota);
            imageNota = itemView.findViewById(R.id.imageNota);
        }

        void setNota(Nota nota) {
            textTitulo.setText(nota.getTitulo());
            if (nota.getSubtitulo().trim().isEmpty()) {
                textSubtitulo.setVisibility(View.GONE);
            } else {
                textSubtitulo.setText(nota.getSubtitulo());
            }
            textDateTime.setText(nota.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNota.getBackground();
            if (nota.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(nota.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if (nota.getImagePath() != null) {
                imageNota.setImageBitmap(BitmapFactory.decodeFile(nota.getImagePath()));
                imageNota.setVisibility(View.VISIBLE);
            } else {
                imageNota.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotas(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()){
                    notas = notaSource;
                } else {
                    ArrayList<Nota> temp = new ArrayList<>();
                    for (Nota nota : notaSource) {
                        if (nota.getTitulo().toLowerCase().contains(searchKeyword.toLowerCase())
                                || nota.getSubtitulo().toLowerCase().contains(searchKeyword.toLowerCase())
                                || nota.getTextNota().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(nota);
                        }
                    }
                    notas = temp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
