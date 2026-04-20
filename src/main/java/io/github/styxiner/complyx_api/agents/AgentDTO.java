package io.github.styxiner.complyx_api.agents;

import java.util.List;
import java.util.UUID;

public class AgentDTO {
	private UUID id;
	private String ip;
	private String hostname;
	private String osName;
	private String osVersion;
	private boolean enabled;
	private List<String> groups;
}
