package io.github.styxiner.complyx_api.regulations;

import java.util.List;
import java.util.UUID;

public class RegulationDetailDTO {
    private UUID id;
    private String name;
    private String pdfPath;
    private List<RegSectionDTO> sections;
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
    public List<RegSectionDTO> getSections() { return sections; }
    public void setSections(List<RegSectionDTO> sections) { this.sections = sections; }
}