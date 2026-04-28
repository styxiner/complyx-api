package io.github.styxiner.complyx_api.agents;

/*
 * DTO para actualizar un grupo de agentes.los campos son opcionales (update parcial)
 */
public class AgentGroupUpdateDTO {

    private String name;
    private String description;

    public AgentGroupUpdateDTO() {
    }

    public AgentGroupUpdateDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}