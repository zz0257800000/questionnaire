package com.example.questionnaire.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

//dao(interface)�����~��jpa @Repository ���y�k
//����eneity (class)��X����vo(class)  ,vo��ifs(interface��@)  ,ifs��@�̫ᱵimpl(class�i���޿�P�_�]�i�Ǩ��ݸ��),�̫�bTest����
//�ݩ�private �����L�ɮ׸�� public�i�H����{���� string�w�]null
@Service
public class QuizServiceImpl implements QuizService {

	@Autowired

	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;

	@Transactional
	@Override
	public QuizRes create(QuizReq req) {
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
		Questionnaire qn = qnDao.save(req.getQuestionnaire());
		int quId = qn.getId();
		List<Question> quList = req.getQuestionList();
		if (quList.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}

		for (Question qu : quList) {
			qu.setQnId(quId);
		}
		quDao.saveAll(quList);
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	private QuizRes checkParam(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();

		if (!StringUtils.hasText(qn.getTitle()) || !StringUtils.hasText(qn.getDescription())
				|| qn.getStartDate() == null || qn.getEndDate() == null || qn.getStartDate().isAfter(qn.getEndDate()))

		{
			return new QuizRes(RtnCode.QUESTIONNAIRE_PARAM_ERROR);
		}

		// �C�Ӱ��D���ˬdfor�j��
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQuId() < 0 || !StringUtils.hasText(qu.getqTitle()) || !StringUtils.hasText(qu.getOptiontype())
					|| !StringUtils.hasText(qu.getOption())) {
				return new QuizRes(RtnCode.QUESTION_PARAM_ERROR);

			}
		}

		return null;

	}

	@Transactional
	@Override
	public QuizRes update(QuizReq req) {
		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}
		checkResult = checkQuestionnaireId(req);
		if (checkResult != null) {
			return checkResult;
		}

		Optional<Questionnaire> qnOp = qnDao.findById(req.getQuestionnaire().getId());
		// �P�_�w�s�b���
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);

		}

		Questionnaire qn = qnOp.get();
		// is_published == false �i��
		// is_published == true + ��e�ɶ��p��start_date

		if (!qn.isPublished() || (qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate()))) {

			qnDao.save(req.getQuestionnaire());
			quDao.saveAll(req.getQuestionList());
			return new QuizRes(RtnCode.SUCCESSFUL);
		}

		return new QuizRes(RtnCode.UPDATE_ERROR);

	}

	private QuizRes checkQuestionnaireId(QuizReq req) {
		if (req.getQuestionnaire().getId() <= 0) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);

		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQnId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
			}
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes deleteQuestionnaire(List<Integer> qnIdList) {
		List<Questionnaire> qnList = qnDao.findByIdIn(qnIdList);
		List<Integer> idList = new ArrayList<>();
		// �ŦX����N�R���ŦX�N���R
		for (Questionnaire qn : qnList) {
			if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
				// qnDao.deleteById(qn.getId());
				idList.add(qn.getId());
			}
		}
		if (!idList.isEmpty()) {
			qnDao.deleteAllById(idList); // �R�ݨ�
			quDao.deleteAllByQnIdIn(0, idList); // �R�ݨ��̪��D��

		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes deleteQuestion(int qnId, List<Integer> quIdList) {
		Optional<Questionnaire> qnOp = qnDao.findById(qnId);
		if (!qnOp.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);

		}
		Questionnaire qn = qnOp.get();
		if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
			quDao.deleteAllByQnIdIn(qnId, quIdList);
		}

		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		title = !StringUtils.hasText(title) ? title : ""; // �T�즡

		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		List<Integer> qnIds = new ArrayList<>();
		for (Questionnaire qu : qnList) {
			qnIds.add(qu.getId());
		}
		List<Question> quList = quDao.findAllByQnIdIn(qnIds);
		List<QuizVo> quizVoList = new ArrayList<>();
		for (Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo();
			vo.setQuestionnaire(qn);
			List<Question> questionList = new ArrayList<>();
			for (Question qu : quList) {
				if(qu.getQnId() == qn.getId()) {
					questionList.add(qu);
				}
			}
			vo.setQuestionList(questionList);
			quizVoList.add(vo);
		}

		return new QuizRes(quizVoList,RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,boolean isPublished) {
		title = !StringUtils.hasText(title) ? title : ""; // �T�즡

		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		List<Questionnaire> qnList = new ArrayList<>();
		if(isPublished) {
			qnList = qnDao.find;
		}eles{
			qnList = qnDao.;
		}
		
		
		return new QuestionnaireRes(qnList,RtnCode.SUCCESSFUL);
	}
	@Override
	public QuestionRes searchQuestionList(int qnId) {
		if(qnId <= 0) {
			return new QuestionRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
		return new QuestionRes(quList,RtnCode.SUCCESSFUL);
	}
}
