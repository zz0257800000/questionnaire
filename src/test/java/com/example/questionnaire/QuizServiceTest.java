package com.example.questionnaire;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.service.ifs.QuizService;

@SpringBootTest
public class QuizServiceTest {

	@Autowired
	private QuizService service;
	@Test
	public void createTest() {
		Questionnaire questionnaire = new Questionnaire("test1","test",false,
				LocalDate.of(2023, 11, 15), LocalDate.of(2023, 11, 30));
		List<Question>questionList = new ArrayList<>();
	}
}
