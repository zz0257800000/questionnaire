package com.example.questionnaire.constants;

public enum RtnCode {
		//�i�H�Ȼs�ƿ��~�T��!!
	  //5�}�Y�q�A��
	SUCCESSFUL(200, "Ok!"),// ���\��200 
	QUESTION_PARAM_ERROR(400,"Question_param_error!"),//401 403 �����v�� 404���~
	QUESTIONNAIRE_PARAM_ERROR(400,"Questionnaire_param_error!"),//401 403 �����v�� 404���~
	QUESTIONNAIRE_ID_PARAM_ERROR(400,"Questionnaire_id_param_error!"),
	QUESTIONNAIRE_ID_NOT_FOUND(404,"Questionnaire_id_not_found!"),
	UPDATE_ERROR(200, "Ok!"),
	
	;
	
	private int code;

	private String message;    

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
