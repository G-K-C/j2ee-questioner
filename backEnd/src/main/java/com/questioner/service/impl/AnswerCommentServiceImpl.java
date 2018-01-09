package com.questioner.service.impl;

import com.questioner.entity.AnswerComment;
import com.questioner.repository.AnswerCommentRepository;
import com.questioner.service.abs.AnswerCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerCommentServiceImpl implements AnswerCommentService{
    @Autowired
    private AnswerCommentRepository answerCommentRepository;

    @Override
    public boolean saveAnswerComment(AnswerComment answerComment) {
        answerCommentRepository.save(answerComment);
        return true;
    }

    @Override
    public Long getCommentCountOfAnswer(Long answerId) {
        return answerCommentRepository.countByAnswerId(answerId);
    }

    @Override
    public List<AnswerComment> getAllCommentsOfAnswer(Long answerId) {
        return answerCommentRepository.findByAnswerId(answerId);
    }
}
