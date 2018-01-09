package com.questioner.service.abs;

import com.questioner.entity.AnswerComment;

import java.util.List;

public interface AnswerCommentService {
    boolean saveAnswerComment(AnswerComment answerComment);

    Long getCommentCountOfAnswer(Long answerId);

    List<AnswerComment> getAllCommentsOfAnswer(Long answerId);
}
