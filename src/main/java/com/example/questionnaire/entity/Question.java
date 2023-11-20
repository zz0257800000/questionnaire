package com.example.questionnaire.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(value = QuestionId.class)
@Table(name = "question")
public class Question {
	@Id
	@Column(name = "id")
	private int quId;
	@Id
	@Column(name = "qn_id")
	private int qnId;
	
	@Column(name = "q_title")
	private String qTitle;
	
	@Column(name = "option_type")
	private String optiontype;
	@Column(name = "is_necessary")
	private String necessary;
	@Column(name = "option")
	private String option;
	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Question(int quId, int qnId, String qTitle, String optiontype, String necessary, String option) {
		super();
		this.quId = quId;
		this.qnId = qnId;
		this.qTitle = qTitle;
		this.optiontype = optiontype;
		this.necessary = necessary;
		this.option = option;
	}
	public int getQuId() {
		return quId;
	}
	public void setQuId(int quId) {
		this.quId = quId;
	}
	public int getQnId() {
		return qnId;
	}
	public void setQnId(int qnId) {
		this.qnId = qnId;
	}
	public String getqTitle() {
		return qTitle;
	}
	public void setqTitle(String qTitle) {
		this.qTitle = qTitle;
	}
	public String getOptiontype() {
		return optiontype;
	}
	public void setOptiontype(String optiontype) {
		this.optiontype = optiontype;
	}
	public String getNecessary() {
		return necessary;
	}
	public void setNecessary(String necessary) {
		this.necessary = necessary;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	
	
	
}
