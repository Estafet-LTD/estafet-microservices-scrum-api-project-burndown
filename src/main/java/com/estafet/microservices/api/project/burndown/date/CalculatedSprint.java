package com.estafet.microservices.api.project.burndown.date;

import static com.estafet.microservices.api.project.burndown.date.DateHelper.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CalculatedSprint {

	private String startDate;

	private String endDate;

	private Integer number;

	private Integer noDays;

	private CalculatedSprint next;

	private CalculatedSprint previous;

	public CalculatedSprint(String startDate, Integer noDays) {
		this(null, startDate, noDays);
	}

	public CalculatedSprint(CalculatedSprint previous, String startDate, Integer noDays) {
		this.noDays = noDays;
		this.startDate = previous == null ? startDate : getNextWorkingDay(increment(previous.endDate));
		this.endDate = calculateEndDate();
		this.number = previous == null ? 1 : previous.number + 1;
		this.previous = previous;
		if (previous != null) {
			this.previous.next = this;
		}
	}

	public CalculatedSprint addSprint() {
		return new CalculatedSprint(getLastSprint(), startDate, noDays);
	}

	private CalculatedSprint getLastSprint() {
		if (next != null) {
			return next.getLastSprint();
		} else {
			return this;
		}
	}

	private CalculatedSprint getFirstSprint() {
		if (previous != null) {
			return previous.getLastSprint();
		} else {
			return this;
		}
	}

	private String calculateEndDate() {
		String day = getNextWorkingDay(startDate);
		int i = 1;
		while (i < noDays) {
			day = getNextWorkingDay(increment(day));
			i++;
		}
		return day;
	}

	@JsonIgnore
	public CalculatedSprint getNext() {
		return next;
	}

	@JsonIgnore
	public CalculatedSprint getPrevious() {
		return previous;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public Integer getNumber() {
		return number;
	}

	public Integer getNoDays() {
		return noDays;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<CalculatedSprint> toList() {
		return toList(getFirstSprint(), new ArrayList<CalculatedSprint>());
	}

	private List<CalculatedSprint> toList(CalculatedSprint calculatedSprint, List<CalculatedSprint> calculatedSprints) {
		calculatedSprints.add(calculatedSprint);
		if (calculatedSprint.next != null) {
			return toList(calculatedSprint.next, calculatedSprints);
		} else {
			return calculatedSprints;
		}
	}

}
