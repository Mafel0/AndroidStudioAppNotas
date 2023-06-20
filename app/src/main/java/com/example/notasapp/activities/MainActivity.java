package com.example.notasapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.notasapp.R;
import com.example.notasapp.adapters.NotasAdapter;
import com.example.notasapp.database.NotasDatabase;
import com.example.notasapp.entidades.Nota;
import com.example.notasapp.listeners.NotasListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements NotasListener {

    public static final int requestAddNota = 1;
    public static final int requestUpdateNota = 2;
    public static final int requestShowNota = 3;

    private RecyclerView notasRecyclerView;
    private List<Nota> notaList;
    private NotasAdapter notasAdapter;

    private int notasClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNotaMain = findViewById(R.id.imageAddNotaMain);
        imageAddNotaMain.setOnClickListener(this::onClick);

        notasRecyclerView = findViewById(R.id.notasRecyclerView);
        notasRecyclerView.setLayoutManager (
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        notaList = new ArrayList<>();
        notasAdapter = new NotasAdapter(notaList, this);
        notasRecyclerView.setAdapter(notasAdapter);

        getNotas(requestShowNota);

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notasAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(notaList.size() != 0) {
                    //anotasAdapter.searchNotas(s.toString());
                }
            }
        });
    }
    private void onClick(View v) {
        startActivityForResult(
                new Intent(getApplicationContext(), criarNotaActivity.class),
                requestAddNota
        );
    }

    @Override
    public void onNoteClicked(Nota nota, int position) {
        notasClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), criarNotaActivity.class);
        intent.putExtra("ViewOuUpdate", true);
        intent.putExtra("nota", nota);
        startActivityForResult(intent, requestUpdateNota);
    }

    private void getNotas(final int requestCode) {

        @SuppressLint("StaticFieldLeak")
        class GetNotasTask extends AsyncTask<Void, Void, List<Nota>> {

            @Override
            protected List<Nota> doInBackground(Void... voids) {
                return NotasDatabase
                        .getDatabase(getApplicationContext())
                        .notaDao().getAllNotas();
            }

            @Override
            protected void onPostExecute(List<Nota> notas) {
                super.onPostExecute(notas);
                if(requestCode == requestShowNota) {
                    notaList.addAll(notas);
                    notasAdapter.notifyDataSetChanged();
                }else if(requestCode == requestAddNota) {
                    notaList.add(0,notas.get(0));
                    notasAdapter.notifyItemInserted(0);
                    notasRecyclerView.smoothScrollToPosition(0);
                }else if(requestCode == requestUpdateNota) {
                    notaList.remove(notasClickedPosition);
                    notaList.add(notasClickedPosition, notas.get(notasClickedPosition));
                    notasAdapter.notifyItemChanged(notasClickedPosition);
                }
            }
        }
        new GetNotasTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestAddNota && resultCode == RESULT_OK) {
            getNotas(requestShowNota);
        }else if(requestCode == requestUpdateNota && resultCode == RESULT_OK) {
            if (data != null) {
                getNotas(requestUpdateNota);
            }
        }
    }
}