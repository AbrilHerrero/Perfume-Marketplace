package com.uade.tpo.marketplacePerfume.adapter.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FragellaFragranceResponse {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Brand")
    private String brand;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Image URL")
    private String imageUrl;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Sillage")
    private String sillage;

    @JsonProperty("rating")
    private String rating;

    @JsonProperty("General Notes")
    private List<String> generalNotes;

    @JsonProperty("Main Accords")
    private List<String> mainAccords;

    @JsonProperty("Main Accords Percentage")
    private Map<String, String> mainAccordsPercentage;

    @JsonProperty("Notes")
    private NotesDetail notes;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NotesDetail {
        @JsonProperty("Top")
        private List<NoteEntry> top;

        @JsonProperty("Middle")
        private List<NoteEntry> middle;

        @JsonProperty("Base")
        private List<NoteEntry> base;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NoteEntry {
        private String name;
        private String imageUrl;
    }
}
