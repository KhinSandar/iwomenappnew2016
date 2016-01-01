package com.smk.model;

import java.util.ArrayList;
import java.util.List;

public class AnswerList {
	
	private List<Answer> answers = new ArrayList<Answer>();
	
	public AnswerList(List<Answer> answers) {
		super();
		this.answers = answers;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	
}
