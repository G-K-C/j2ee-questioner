package com.questioner.service.impl;

import com.questioner.entity.Account;
import com.questioner.entity.QuestionType;
import com.questioner.repository.AccountRepository;
import com.questioner.repository.QuestionTypeRepository;
import com.questioner.service.abs.QuestionTypeService;
import com.questioner.util.PageableBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionTypeServiceImpl implements QuestionTypeService{

    @Autowired
    private QuestionTypeRepository questionTypeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Iterable<QuestionType> getAllQuestionType() {
        return questionTypeRepository.findAll();
    }

    @Override
    public boolean modifyQuestionType(Long questionTypeId, String questionCourse, String questionSubject){
        QuestionType modifyQuestionType=questionTypeRepository.findOne(questionTypeId);
        if(modifyQuestionType!=null) {
            modifyQuestionType.setCourse(questionCourse);
            modifyQuestionType.setSubject(questionSubject);
            questionTypeRepository.save(modifyQuestionType);
            return true;
        }
        return false;
    }

    @Override
    public boolean addQuestionType(String questionCourse, String questionSubject){
        QuestionType addQuestionType=new QuestionType();
        if(addQuestionType!=null) {
            addQuestionType.setCourse(questionCourse);
            addQuestionType.setSubject(questionSubject);
            questionTypeRepository.save(addQuestionType);
            return true;
        }
        return false;
    }

    @Override
    public QuestionType getQuestionType(Long questionTypeId) {
        return questionTypeRepository.findOne(questionTypeId);
    }

    @Override
    public boolean hasFollowQuestionType(Long questionTypeId, Long userId) {
        return questionTypeRepository.hasFollowQuestionType(userId, questionTypeId) > 0;
    }

    @Override
    public boolean followQuestionType(Long questionTypeId, Long userId) {
        Account account = accountRepository.findOne(userId);
        if (account != null) {
            QuestionType questionType = questionTypeRepository.findOne(questionTypeId);
            if (questionType != null) {
                account.getFollowQuestionType().add(questionType);
                accountRepository.save(account);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean unFollowQuestionType(Long questionTypeId, Long userId) {
        Account account = accountRepository.findOne(userId);
        if (account != null) {
            List<QuestionType> questionTypes = account.getFollowQuestionType();
           questionTypes.removeIf(questionType -> questionType.getId().equals(questionTypeId));
           accountRepository.save(account);
           return true;
        }
        return false;
    }

    @Override
    public Page<QuestionType> getUserFollowQuestionType(Long userId, int currentPage, int pageSize) {
        Pageable pageable = new PageableBuilder().setCurrentPage(currentPage)
                .setPageSize(pageSize).setDirection(Sort.Direction.DESC).setSortParam("id").buildPage();
        return questionTypeRepository.getUserFollowQuestionType(userId, pageable);
    }
}
