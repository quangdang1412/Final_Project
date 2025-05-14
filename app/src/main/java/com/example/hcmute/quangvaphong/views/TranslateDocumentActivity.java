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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.TranslationResponse;
import com.google.gson.Gson;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
                            // Process image directly with OCR
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
                                // Process the gallery image directly with OCR
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

            String sourceLang = sourceLanguage.getText().toString();
            String targetLang = null;
            if (sourceLang.equals("Tiếng Việt")) {
                sourceLang = "vi";
                targetLang = "en";
            } else {
                sourceLang = "en";
                targetLang = "vi";
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

    // Open camera app
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
        // Show processing message
        Toast.makeText(this, "Processing image, please wait...", Toast.LENGTH_SHORT).show();

        // Clear previous text and disable translation button during processing
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
                        String sourceLang = sourceLanguage.getText().toString();
                        String targetLang;
                        if (sourceLang.equals("Tiếng Việt")) {
                            sourceLang = "vi";
                            targetLang = "en";
                        } else {
                            sourceLang = "en";
                            targetLang = "vi";
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

        final int MAX_CHUNK_SIZE = 450;

        if (text.length() <= MAX_CHUNK_SIZE) {
            translateTextChunk(text, sourceLang, targetLang, true);
        } else {
            List<String> chunks = splitIntoChunks(text, MAX_CHUNK_SIZE);
            final StringBuilder completeTranslation = new StringBuilder();
            final int[] chunkCounter = { 0 };

            Toast.makeText(TranslateDocumentActivity.this,
                    "Text is long, splitting into " + chunks.size() + " parts for translation",
                    Toast.LENGTH_SHORT).show();

            processNextChunk(chunks, sourceLang, targetLang, completeTranslation, chunkCounter);
        }
    }

    private void processNextChunk(List<String> chunks, String sourceLang, String targetLang,
            StringBuilder completeTranslation, int[] chunkCounter) {
        if (chunkCounter[0] < chunks.size()) {
            String chunk = chunks.get(chunkCounter[0]);
            boolean isLastChunk = (chunkCounter[0] == chunks.size() - 1);

            translateTextChunk(chunk, sourceLang, targetLang, isLastChunk, new TranslationCallback() {
                @Override
                public void onTranslationComplete(String translatedChunk) {
                    completeTranslation.append(translatedChunk).append(" ");
                    outputText.setText(completeTranslation.toString().trim());

                    chunkCounter[0]++;
                    processNextChunk(chunks, sourceLang, targetLang, completeTranslation, chunkCounter);
                }

                @Override
                public void onTranslationError(String errorMessage) {
                    Toast.makeText(TranslateDocumentActivity.this,
                            "Error in chunk " + (chunkCounter[0] + 1) + ": " + errorMessage,
                            Toast.LENGTH_SHORT).show();

                    chunkCounter[0]++;
                    processNextChunk(chunks, sourceLang, targetLang, completeTranslation, chunkCounter);
                }
            });
        } else {
            translateButton.setEnabled(true);
        }
    }

    private interface TranslationCallback {
        void onTranslationComplete(String translatedText);

        void onTranslationError(String errorMessage);
    }

    private void translateTextChunk(String text, String sourceLang, String targetLang, boolean isLastChunk) {
        translateTextChunk(text, sourceLang, targetLang, isLastChunk, null);
    }

    private void translateTextChunk(String text, String sourceLang, String targetLang,
            boolean isLastChunk, TranslationCallback callback) {
        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");

            String url = "https://api.mymemory.translated.net/get?q=" + encodedText +
                    "&langpair=" + sourceLang + "|" + targetLang;

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Gson gson = new Gson();
                                TranslationResponse translationResponse = gson.fromJson(response,
                                        TranslationResponse.class);

                                if (translationResponse != null && translationResponse.getResponseData() != null) {
                                    String translatedText = translationResponse.getResponseData().getTranslatedText();

                                    if (callback != null) {
                                        callback.onTranslationComplete(translatedText);
                                    } else {
                                        outputText.setText(translatedText);
                                        if (isLastChunk) {
                                            translateButton.setEnabled(true);
                                        }
                                    }
                                } else {
                                    if (callback != null) {
                                        callback.onTranslationError("Translation failed");
                                    } else {
                                        Toast.makeText(TranslateDocumentActivity.this,
                                                "Translation failed. Please try again.",
                                                Toast.LENGTH_SHORT).show();
                                        outputText.setText(R.string.translation_error);
                                        if (isLastChunk) {
                                            translateButton.setEnabled(true);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                if (callback != null) {
                                    callback.onTranslationError(e.getMessage());
                                } else {
                                    Toast.makeText(TranslateDocumentActivity.this,
                                            "Error parsing response: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    outputText.setText(R.string.translation_error);
                                    if (isLastChunk) {
                                        translateButton.setEnabled(true);
                                    }
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TranslateDocumentActivity.this,
                                    "Network error: " + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            outputText.setText(R.string.translation_error);

                            translateButton.setEnabled(true);
                        }
                    });

            queue.add(stringRequest);

        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Error encoding text: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            translateButton.setEnabled(true);
        }
    }

    private List<String> splitIntoChunks(String text, int maxChunkSize) {
        List<String> chunks = new ArrayList<>();

        if (text.length() <= maxChunkSize) {
            chunks.add(text);
            return chunks;
        }

        String[] sentences = text.split("(?<=[.!?\\n])\\s+");
        StringBuilder currentChunk = new StringBuilder();

        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() > maxChunkSize && currentChunk.length() > 0) {
                chunks.add(currentChunk.toString().trim());
                currentChunk = new StringBuilder(sentence);
            } else {
                if (currentChunk.length() > 0) {
                    currentChunk.append(" ");
                }
                currentChunk.append(sentence);
            }
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }

        List<String> finalChunks = new ArrayList<>();
        for (String chunk : chunks) {
            if (chunk.length() <= maxChunkSize) {
                finalChunks.add(chunk);
            } else {
                for (int i = 0; i < chunk.length(); i += maxChunkSize) {
                    int end = Math.min(i + maxChunkSize, chunk.length());
                    finalChunks.add(chunk.substring(i, end));
                }
            }
        }

        return finalChunks;
    }
}
