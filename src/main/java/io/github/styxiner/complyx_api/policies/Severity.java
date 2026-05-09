package io.github.styxiner.complyx_api.policies;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Nivel de severidad en caso de incumplimiento")
public enum Severity {
	CRITICAL,
	HIGH,
	MEDIUM,
	LOW
}
