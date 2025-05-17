package com.example.hcmute.quangvaphong.views;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hcmute.quangvaphong.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DictionaryActivity extends AppCompatActivity {
    private TextView wordTitle;
    private WebView dictionaryContent;
    private ProgressBar loadingProgressBar;
    private View rootLayout;
    private String selectedWord;
    private TextToSpeech textToSpeech;
    private static final String PREFS_NAME = "DictionaryPrefs";
    private static final String HISTORY_KEY = "searchHistory";
    private static final int MAX_HISTORY_ITEMS = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        wordTitle = findViewById(R.id.word_title);
        dictionaryContent = findViewById(R.id.dictionary_content);
        loadingProgressBar = findViewById(R.id.loading_progress);
        MaterialButton btnPronunciation = findViewById(R.id.btn_pronunciation);
        rootLayout = findViewById(R.id.dictionary_coordinator_layout);
        ImageView backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> finish());

        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            } else {
                Snackbar.make(rootLayout, "Text-to-speech initialization failed", Snackbar.LENGTH_SHORT).show();
            }
        });
        if (getIntent().hasExtra("word")) {
            selectedWord = getIntent().getStringExtra("word");
            wordTitle.setText(selectedWord);

            addToSearchHistory(selectedWord);

            new FetchDictionaryTask().execute(selectedWord);

            if (getIntent().getBooleanExtra("should_pronounce", false)) {
                new android.os.Handler().postDelayed(() -> {
                    if (textToSpeech != null && !selectedWord.isEmpty()) {
                        textToSpeech.speak(selectedWord, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }, 500); 
            }
        }
        btnPronunciation.setOnClickListener(v -> {
            if (selectedWord != null && !selectedWord.isEmpty()) {
                if (textToSpeech != null) {
                    textToSpeech.speak(selectedWord, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void addToSearchHistory(String word) {
        if (word == null || word.isEmpty())
            return;

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet(HISTORY_KEY, new HashSet<>());

        Set<String> updatedHistory = new HashSet<>(historySet);

        updatedHistory.remove(word);
        updatedHistory.add(word);

        if (updatedHistory.size() > MAX_HISTORY_ITEMS) {
            List<String> historyList = new ArrayList<>(updatedHistory);
            historyList = historyList.subList(historyList.size() - MAX_HISTORY_ITEMS, historyList.size());
            updatedHistory = new HashSet<>(historyList);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(HISTORY_KEY, updatedHistory);
        editor.apply();
    }

    private void showSearchHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> historySet = prefs.getStringSet(HISTORY_KEY, new HashSet<>());

        if (historySet.isEmpty()) {
            Toast.makeText(this, "No search history yet", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> historyList = new ArrayList<>(historySet);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search History");
        builder.setItems(historyList.toArray(new String[0]), (dialog, which) -> {
            String selectedItem = historyList.get(which);
            selectedWord = selectedItem;
            wordTitle.setText(selectedWord);

            new FetchDictionaryTask().execute(selectedWord);
        });

        builder.setNegativeButton("Close", null);
        builder.setNeutralButton("Clear History", (dialog, which) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(HISTORY_KEY);
            editor.apply();
            Toast.makeText(DictionaryActivity.this, "History cleared", Toast.LENGTH_SHORT).show();
        });

        builder.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchDictionaryTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            loadingProgressBar.setVisibility(View.VISIBLE);
            dictionaryContent.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            String word = params[0];
            String url = "https://dictionary.cambridge.org/vi/dictionary/english-vietnamese/" + word.toLowerCase();

            try {
                Document doc = Jsoup.connect(url)
                        .userAgent(
                                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .get();

                Elements dictionaryDiv = doc.select("div.sense-block.pr.dsense.dsense-noh");
                if (dictionaryDiv != null && !dictionaryDiv.isEmpty()) {
                    String css = "<style>"
                            + "body { font-family: 'Roboto', 'Segoe UI', Arial, sans-serif; padding: 16px; line-height: 1.8; color: #333; font-size: 130%; }"
                            + ".pr { margin-bottom: 30px; border-bottom: 2px solid #ddd; padding-bottom: 25px; }"
                            + ".di { padding: 14px; }"
                            + ".dsense { background-color: #fafafa; border-radius: 12px; padding: 20px; margin-bottom: 25px; box-shadow: 0 3px 6px rgba(0,0,0,0.15); }"
                            + ".hw { font-weight: bold; font-size: 2.2em; color: #2962FF; margin-bottom: 16px; display: block; }"
                            + ".pos { color: #616161; font-style: italic; background-color: #f5f5f5; padding: 6px 10px; border-radius: 6px; font-size: 1.3em; }"
                            + ".def { margin: 16px 0; font-size: 1.5em; line-height: 1.8; }"
                            + ".eg { color: #546E7A; margin: 14px 0 14px 24px; font-style: italic; display: block; padding-left: 16px; border-left: 4px solid #E0E0E0; font-size: 1.35em; }"
                            + ".trans { color: #00695C; font-weight: 600; font-size: 1.4em; }"
                            + ".dexamp { background-color: #F5F5F5; border-radius: 8px; padding: 16px; margin: 16px 0; }"
                            + ".sense-body { padding-left: 24px; }"
                            + ".phrase-body { padding-left: 24px; border-left: 5px solid #E0E0E0; }"
                            + ".phrase-title, .phrase-info { font-weight: bold; color: #1976D2; font-size: 1.4em; }"
                            + ".irreg-infls { font-style: italic; color: #7B1FA2; font-size: 1.3em; }"
                            + "</style>";

                    return css + dictionaryDiv.outerHtml();
                } else {
                    return "<div style='font-family: \"Roboto\", sans-serif; color: #F44336; padding: 40px; text-align: center; background: #FBE9E7; border-radius: 12px; margin: 25px; font-size: 130%;'>"
                            + "<h2 style='font-size: 1.8em;'>😕 No definition found</h2>"
                            + "<p style='font-size: 1.3em;'>We couldn't find a definition for \"<b>" + word
                            + "</b>\" in our dictionary.</p>"
                            + "<p style='font-size: 1.3em;'>Try checking the spelling or searching for a similar word.</p>"
                            + "</div>";
                }
            } catch (IOException e) {
                e.printStackTrace();

                try {
                    String altUrl = "https://www.dict.com/english-vietnamese/" + word.toLowerCase();
                    Document altDoc = Jsoup.connect(altUrl)
                            .userAgent(
                                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                            .get();

                    Elements altDictionaryDiv = altDoc.select("div.entry");
                    if (altDictionaryDiv != null && !altDictionaryDiv.isEmpty()) {
                        String css = "<style>"
                                + "body { font-family: 'Roboto', 'Segoe UI', Arial, sans-serif; padding: 20px; line-height: 1.8; color: #333; font-size: 130%; }"
                                + ".entry { background-color: #f8f9fa; border-radius: 12px; padding: 24px; margin: 20px 0; box-shadow: 0 4px 8px rgba(0,0,0,0.16); }"
                                + "h3 { color: #1976D2; margin-bottom: 20px; font-size: 2em; }"
                                + ".pair { margin: 16px 0; padding: 8px 0; }"
                                + ".from { font-weight: bold; color: #2962FF; font-size: 1.6em; }"
                                + ".to { color: #00695C; margin-left: 16px; font-size: 1.5em; font-weight: 500; }"
                                + "</style>";

                        return css + "<h3>Alternative Dictionary Source:</h3>" + altDictionaryDiv.outerHtml();
                    }
                } catch (Exception ignored) {
                }
                return "<div style='font-family: \"Roboto\", sans-serif; color: #F44336; padding: 40px; text-align: center; background: #FBE9E7; border-radius: 12px; margin: 25px; font-size: 130%;'>"
                        + "<h2 style='font-size: 1.8em;'>⚠️ Connection Error</h2>"
                        + "<p style='font-size: 1.3em;'>We couldn't connect to the dictionary service.</p>"
                        + "<p style='font-size: 1.3em;'>Error details: " + e.getMessage() + "</p>"
                        + "<p style='font-size: 1.3em;'>Please check your internet connection and try again.</p>"
                        + "<button onclick='window.location.reload();' style='background: #FF5722; color: white; border: none; padding: 14px 20px; border-radius: 8px; margin-top: 20px; font-size: 1.2em;'>Try Again</button>"
                        + "</div>";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            loadingProgressBar.setVisibility(View.GONE);
            dictionaryContent.setVisibility(View.VISIBLE);
            dictionaryContent.getSettings().setJavaScriptEnabled(false);
            dictionaryContent.getSettings().setBuiltInZoomControls(true);
            dictionaryContent.getSettings().setDisplayZoomControls(false);
            dictionaryContent.getSettings().setLoadWithOverviewMode(true);
            dictionaryContent.getSettings().setUseWideViewPort(true);
            dictionaryContent.getSettings().setDefaultFontSize(22);

            dictionaryContent.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null);

            dictionaryContent.requestFocus();
        }
    }
}
