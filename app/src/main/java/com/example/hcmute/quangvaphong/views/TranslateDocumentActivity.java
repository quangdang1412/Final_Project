package com.example.hcmute.quangvaphong.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.hcmute.quangvaphong.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TranslateDocumentActivity extends AppCompatActivity {

    private EditText inputText;
    private TextView outputText;
    private TextView sourceLanguage;
    private TextView targetLanguage;
    private ImageView backButton;
    private ImageView cameraIcon;
    private ImageView mediaIcon;
    private ImageView swapLanguages;
    private CardView translateButton;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_GALLERY_PERMISSION = 101;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private TextRecognizer textRecognizer;

    private Map<String, Translator> translators = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_document);

        inputText = findViewById(R.id.input_text);
        outputText = findViewById(R.id.output_text);
        sourceLanguage = findViewById(R.id.source_language);
        targetLanguage = findViewById(R.id.target_language);
        backButton = findViewById(R.id.back_button);
        cameraIcon = findViewById(R.id.camera_icon);
        mediaIcon = findViewById(R.id.media_icon);
        swapLanguages = findViewById(R.id.swap_languages);
        translateButton = findViewById(R.id.translate_button);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        if (imageBitmap != null) {
                            processImageForOcr(imageBitmap);
                        }
                    }
                });
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                        this.getContentResolver(), selectedImageUri);
                                processImageForOcr(bitmap);
                            } catch (IOException e) {
                                Toast.makeText(this, "Error loading image: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        backButton.setOnClickListener(view -> finish());

        cameraIcon.setOnClickListener(view -> {
            requestCameraPermissionAndOpenCamera();
        });

        mediaIcon.setOnClickListener(view -> {
            requestGalleryPermissionAndOpenGallery();
        });

        swapLanguages.setOnClickListener(view -> {
            String tempLang = sourceLanguage.getText().toString();
            sourceLanguage.setText(targetLanguage.getText());
            targetLanguage.setText(tempLang);

            if (!outputText.getText().toString().equals(getString(R.string.translation_result))) {
                String tempText = inputText.getText().toString();
                inputText.setText(outputText.getText().toString());
                outputText.setText(tempText);
            }
        });

        translateButton.setOnClickListener(view -> {
            String text = inputText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter text to translate", Toast.LENGTH_SHORT).show();
                return;
            }
            String sourceText = sourceLanguage.getText().toString();
            final String sourceLang;
            final String targetLang;
            if (sourceText.equals("Tiếng Việt")) {
                sourceLang = TranslateLanguage.VIETNAMESE;
                targetLang = TranslateLanguage.ENGLISH;
            } else {
                sourceLang = TranslateLanguage.ENGLISH;
                targetLang = TranslateLanguage.VIETNAMESE;
            }

            translateText(text, sourceLang, targetLang);
        });
    }

    private void requestCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(cameraIntent);
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestGalleryPermissionAndOpenGallery() {
        String permission;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { permission }, REQUEST_GALLERY_PERMISSION);
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void processImageForOcr(Bitmap bitmap) {
        Toast.makeText(this, "Processing image, please wait...", Toast.LENGTH_SHORT).show();

        inputText.setText("");
        translateButton.setEnabled(false);

        InputImage image = InputImage.fromBitmap(bitmap, 0);
        textRecognizer.process(image)
                .addOnSuccessListener(textBlocks -> {
                    String recognizedText = textBlocks.getText();
                    if (recognizedText.isEmpty()) {
                        Toast.makeText(TranslateDocumentActivity.this,
                                "No text detected in image", Toast.LENGTH_SHORT).show();
                        translateButton.setEnabled(true);
                    } else {
                        inputText.setText(recognizedText);
                        String sourceText = sourceLanguage.getText().toString();
                        final String sourceLang;
                        final String targetLang;
                        if (sourceText.equals("Tiếng Việt")) {
                            sourceLang = TranslateLanguage.VIETNAMESE;
                            targetLang = TranslateLanguage.ENGLISH;
                        } else {
                            sourceLang = TranslateLanguage.ENGLISH;
                            targetLang = TranslateLanguage.VIETNAMESE;
                        }
                        translateText(recognizedText, sourceLang, targetLang);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TranslateDocumentActivity.this,
                            "Error processing image: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void translateText(String text, String sourceLang, String targetLang) {
        translateButton.setEnabled(false);
        outputText.setText("");

        String translatorKey = sourceLang + "-" + targetLang;
        Translator translator = translators.get(translatorKey);

        if (translator == null) {
            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLang)
                    .setTargetLanguage(targetLang)
                    .build();

            translator = Translation.getClient(options);
            translators.put(translatorKey, translator);
            DownloadConditions conditions = new DownloadConditions.Builder()
                    .build();

            final Translator finalTranslator = translator;
            final String finalText = text;

            translator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(TranslateDocumentActivity.this,
                                "Model downloaded successfully", Toast.LENGTH_SHORT).show();
                        performTranslation(finalTranslator, finalText);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(TranslateDocumentActivity.this,
                                "Failed to download language model: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        translateButton.setEnabled(true);
                    });
        } else {
            performTranslation(translator, text);
        }
    }

    private void performTranslation(Translator translator, String text) {
        Toast.makeText(this, "Translating text...", Toast.LENGTH_SHORT).show();

        translator.translate(text)
                .addOnSuccessListener(translatedText -> {
                    outputText.setText(translatedText);
                    translateButton.setEnabled(true);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TranslateDocumentActivity.this,
                            "Translation error: " + e.getMessage() + ". Trying to fix...",
                            Toast.LENGTH_SHORT).show();

                    DownloadConditions conditions = new DownloadConditions.Builder().build();
                    translator.downloadModelIfNeeded(conditions)
                            .addOnSuccessListener(unused -> {
                                translator.translate(text)
                                        .addOnSuccessListener(translatedText -> {
                                            outputText.setText(translatedText);
                                            translateButton.setEnabled(true);
                                        })
                                        .addOnFailureListener(e2 -> {
                                            Toast.makeText(TranslateDocumentActivity.this,
                                                    "Translation failed: " + e2.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                            outputText.setText(R.string.translation_error);
                                            translateButton.setEnabled(true);
                                        });
                            })
                            .addOnFailureListener(e2 -> {
                                Toast.makeText(TranslateDocumentActivity.this,
                                        "Failed to recover: " + e2.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                outputText.setText(R.string.translation_error);
                                translateButton.setEnabled(true);
                            });
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Translator translator : translators.values()) {
            translator.close();
        }
        translators.clear();
    }
}
