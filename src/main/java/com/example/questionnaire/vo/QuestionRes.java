package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;

public class QuestionRes {
	private List<Question> questionList;
	private RtnCode rtnCode;
	public QuestionRes() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QuestionRes(List<Question> questionList, RtnCode rtnCode) {
		super();
		this.questionList = questionList;
		this.rtnCode = rtnCode;
	}
	public List<Question> getQuestionList() {
		return questionList;
	}
	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}
	public RtnCode getRtnCode() {
		return rtnCode;
	}
	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	
}
