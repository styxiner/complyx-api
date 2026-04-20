package io.github.styxiner.complyx_api.agents;

import java.time.LocalDateTime;
import java.util.Set;

public class AgentEntity {
	private String ip;
	private String hostname;
	private String osName;
	private String osVersion;
	private LocalDateTime installDate;
	private LocalDateTime latestConnection;
	private boolean enabled;
	private Set<AgentGroupEntity> groups;
}
