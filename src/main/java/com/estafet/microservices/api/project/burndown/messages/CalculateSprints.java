package com.estafet.microservices.api.project.burndown.messages;

public class CalculateSprints {

	private Integer projectId;

	private int noDays;

	private int noSprints;

	public CalculateSprints(Integer projectId, int noDays, int noSprints) {
		this.projectId = projectId;
		this.noDays = noDays;
		this.noSprints = noSprints;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public int getNoDays() {
		return noDays;
	}

	public int getNoSprints() {
		return noSprints;
	}

}
