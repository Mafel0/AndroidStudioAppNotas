package com.example.notasapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notasapp.R;
import com.example.notasapp.database.NotasDatabase;
import com.example.notasapp.entidades.Nota;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class criarNotaActivity extends AppCompatActivity {

    private EditText inputNotaTitulo, inputNotaSubtitulo, inputNotaText;
    private TextView textDateTime;
    private View viewSubtituloIndicador;
    private ImageView imageNota;

    private String selectedNotaColor;
    private String selectedImagePath;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private Nota notaDisponivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nota);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener((v) -> onBackPressed());

        inputNotaTitulo = findViewById(R.id.inputNotaTitulo);
        inputNotaSubtitulo = findViewById(R.id.inputNotaSubtitulo);
        inputNotaText = findViewById(R.id.inputNota);
        textDateTime = findViewById(R.id.textDateTime);
        viewSubtituloIndicador = findViewById(R.id.viewSubtituloIndicador);
        imageNota = findViewById(R.id.imageNota);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", new Locale("pt", "BR"))
                        .format(new Date())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener((v) -> saveNota());

        selectedNotaColor = "#333333";
        selectedImagePath = "";

        InitNotaOptions();
        setSubtituloIndicadorColor();

        if (getIntent().getBooleanExtra("ViewOuUpdate", false)){
            notaDisponivel = (Nota)getIntent().getSerializableExtra("nota");
            setUpdateNota();
        }
    }

    private void InitNotaOptions(){
        final LinearLayout layoutNotaOptions = findViewById(R.id.layoutNotaOptions);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutNotaOptions);
        layoutNotaOptions.findViewById(R.id.textNotaOptions).setOnClickListener((v) -> {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
        });

        final ImageView imageColor1 = layoutNotaOptions.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutNotaOptions.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutNotaOptions.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutNotaOptions.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutNotaOptions.findViewById(R.id.imageColor5);
        final ImageView imageColor6 = layoutNotaOptions.findViewById(R.id.imageColor6);

        layoutNotaOptions.findViewById(R.id.viewColor1).setOnClickListener((v) -> {
                selectedNotaColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtituloIndicadorColor();
        });

        layoutNotaOptions.findViewById(R.id.viewColor2).setOnClickListener((v) -> {
                selectedNotaColor = "#DBFFEC8B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtituloIndicadorColor();
        });

        layoutNotaOptions.findViewById(R.id.viewColor3).setOnClickListener((v) -> {
                selectedNotaColor = "#DBA1EF9F";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtituloIndicadorColor();
        });

        layoutNotaOptions.findViewById(R.id.viewColor4).setOnClickListener((v) -> {
                selectedNotaColor = "#FFA1D0";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtituloIndicadorColor();
        });

        layoutNotaOptions.findViewById(R.id.viewColor5).setOnClickListener((v) -> {
                selectedNotaColor = "#DBE8ABFF";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                imageColor6.setImageResource(0);
                setSubtituloIndicadorColor();
        });

        layoutNotaOptions.findViewById(R.id.viewColor6).setOnClickListener((v) -> {
                selectedNotaColor = "#99DFFF";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(R.drawable.ic_done);
                setSubtituloIndicadorColor();
        });

        layoutNotaOptions.findViewById(R.id.layoutAddImagem).setOnClickListener((v) -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        criarNotaActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION
                );
            } else {
                selectImagem();
            }
        });

    }

    private void setSubtituloIndicadorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtituloIndicador.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNotaColor));
    }



    private void selectImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Erro!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImagem();
            } else {
                Toast.makeText(this, "Permissão Negada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {

                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNota.setImageBitmap(bitmap);
                        imageNota.setVisibility(View.VISIBLE);

                        selectedImagePath = getPathFromUri(selectedImageUri);

                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    private void setUpdateNota() {
        inputNotaTitulo.setText(notaDisponivel.getTitulo());
        inputNotaSubtitulo.setText(notaDisponivel.getSubtitulo());
        inputNotaText.setText(notaDisponivel.getTextNota());
        textDateTime.setText(notaDisponivel.getDateTime());
    }

    private void saveNota() {
        if (inputNotaTitulo.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "O título deve ser preenchido!", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputNotaSubtitulo.getText().toString().trim().isEmpty()
                && inputNotaText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,"A Nota deve ser preenchida!", Toast.LENGTH_SHORT).show();
            return;
        }

        final Nota nota = new Nota();
        nota.setTitulo(inputNotaTitulo.getText().toString());
        nota.setSubtitulo(inputNotaSubtitulo.getText().toString());
        nota.setTextNota(inputNotaText.getText().toString());
        nota.setDateTime(textDateTime.getText().toString());
        nota.setColor(selectedNotaColor);
        nota.setImagePath(selectedImagePath);

        if (notaDisponivel != null) {
            nota.setId(notaDisponivel.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNotaTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotasDatabase.getDatabase(getApplicationContext()).notaDao().insertNota(nota);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        new SaveNotaTask().execute();
    }
}