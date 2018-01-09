package com.questioner.service.abs;

import java.util.List;

import com.questioner.entity.Answer;
import org.springframework.data.domain.Page;

public interface AnswerService {

    int getAnswerNumOfQuestion(Long questionId);

    List<Answer> getLimitAnswers(Long questionId, int startIndex, int limitNum, String sortParam);

    boolean saveAnswer(Answer answer);

    boolean userHasFeedback(Long answerId, Long userId);

    boolean giveAnswerFeedBack(Long answerId, Long userId, boolean isGood);

    Long getQuestionPublisherByAnswer(Long answerId);

    boolean acceptAnswer(Long answerId);

    Page<Answer> getUserAnswersByDateTime(Long userId, int currentPage, int pageSize);

    Page<Answer> getUserAnswersByThumbsUpCount(Long userId, int currentPage, int pageSize);

    Answer getUserAnswerByQuestionId(Long questionId, Long userId);

    Page<Answer> getHiddenAnswerByDateTime(int currentPage, int pageSize);

    Page<Answer> getHiddenAnswerByThumbsUpCount(int currentPage, int pageSize);

    Answer getAnswer(Long answerId);

    Long getUserAnswerCount(Long userId);

    Answer save(Answer answer);
}
