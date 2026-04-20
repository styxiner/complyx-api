package io.github.styxiner.complyx_api.policies;

import lombok.Data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyElementEntity {
	private String name;
	private List<PolicyCheckEntity> checks;
}
