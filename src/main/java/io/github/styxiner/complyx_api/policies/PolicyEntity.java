package io.github.styxiner.complyx_api.policies;

import lombok.Data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolicyEntity {
	private String name;
	private String version;
	private String description;
	private Severity severity;
	private List<PolicyElementDTO> elements;
}
