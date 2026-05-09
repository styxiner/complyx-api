package io.github.styxiner.complyx_api.regulations;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegulationSummaryDTO {
    private UUID id;
    private String name;
    private LocalDateTime addedDate;
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }
}