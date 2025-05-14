package com.example.hcmute.quangvaphong.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslationResponse {
    @SerializedName("responseData")
    private ResponseData responseData;

    @SerializedName("quotaFinished")
    private boolean quotaFinished;

    @SerializedName("mtLangSupported")
    private boolean mtLangSupported;

    @SerializedName("responseDetails")
    private String responseDetails;

    @SerializedName("responseStatus")
    private int responseStatus;

    @SerializedName("matches")
    private List<Match> matches;

    public static class ResponseData {
        @SerializedName("translatedText")
        private String translatedText;

        @SerializedName("match")
        private double match;

        public String getTranslatedText() {
            return translatedText;
        }

        public double getMatch() {
            return match;
        }
    }

    public static class Match {
        @SerializedName("id")
        private String id;

        @SerializedName("segment")
        private String segment;

        @SerializedName("translation")
        private String translation;

        @SerializedName("quality")
        private String quality;

        @SerializedName("reference")
        private String reference;

        @SerializedName("usage-count")
        private int usageCount;

        @SerializedName("subject")
        private String subject;

        @SerializedName("created-by")
        private String createdBy;

        @SerializedName("last-updated-by")
        private String lastUpdatedBy;

        @SerializedName("match")
        private double match;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public boolean isQuotaFinished() {
        return quotaFinished;
    }

    public boolean isMtLangSupported() {
        return mtLangSupported;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public List<Match> getMatches() {
        return matches;
    }
}
