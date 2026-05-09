package io.github.styxiner.complyx_api.regulations;

import java.util.UUID;

public class RegSectionDTO {
    private UUID id;
    private String title;
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}