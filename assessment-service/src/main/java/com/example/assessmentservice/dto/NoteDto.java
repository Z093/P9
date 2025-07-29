package com.example.assessmentservice.dto;

public class NoteDto {
    private String content;

    public NoteDto() {
    }

    public NoteDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
