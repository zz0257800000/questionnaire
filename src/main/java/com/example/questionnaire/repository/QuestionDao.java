package com.example.questionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.QuestionId;

//QuestionId   @Id使用兩個
@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionId> {

	public void deleteAllByQnIdIn(int qnId, List<Integer> quIdList);

	public List<Question> findByQuIdInAndQnId(List<Integer> IdList,int qnId);
	public List<Question> findAllByQnIdIn(List<Integer> qnIdList);

}
