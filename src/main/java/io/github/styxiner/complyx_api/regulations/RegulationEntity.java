package io.github.styxiner.complyx_api.regulations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "regulations")
public class RegulationEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(name = "pdf_path")
	private String pdfPath;

	@Column(name = "added_date", nullable = false, updatable = false)
	private LocalDateTime addedDate = LocalDateTime.now();

	@Column(name = "last_modification", nullable = false)
	private LocalDateTime lastModification = LocalDateTime.now();

	@OneToMany(mappedBy = "regulation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RegulationSectionEntity> sections;

	public RegulationEntity() {
	}

	public RegulationEntity(String name) {
		this.name = name;
	}

	public RegulationEntity(UUID id, String name, String pdfPath, List<RegulationSectionEntity> sections) {
		this.id = id;
		this.name = name;
		this.pdfPath = pdfPath;
		this.sections = sections;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	public LocalDateTime getAddedDate() {
		return addedDate;
	}

	public LocalDateTime getLastModification() {
		return lastModification;
	}

	public void setLastModification(LocalDateTime lastModification) {
		this.lastModification = lastModification;
	}

	public List<RegulationSectionEntity> getSections() {
		return sections;
	}

	public void setSections(List<RegulationSectionEntity> sections) {
		this.sections = sections;
	}
}