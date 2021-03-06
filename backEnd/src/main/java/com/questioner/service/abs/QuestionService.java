package com.questioner.service.abs;

import com.questioner.entity.Question;
import org.springframework.data.domain.Page;
import com.questioner.util.NoticeType;

public interface QuestionService {
    boolean saveQuestion(Question question);

    Long getAnswerNumber(Long questionId);

    Question getQuestion(Long questionId);

    Page<Question> getAllQuestionByPage(int pageSize, int currentPage,String sortParam);

    Page<Question> getQuestionTitleLike(String questionTitle, int pageSize, int currentPage, String sortParam);

    Page<Question> getHiddenQuestionByPage(int pageSize, int currentPage,String sortParam);

    Page<Question> getHiddenQuestionTitleLike(String questionTitle, int pageSize, int currentPage, String sortParam);

    Page<Question> getQuestionByPageAndType(Long typeId, int pageSize, int currentPage, String sortParam);

    Page<Question> getQuestionTitleLikeByType(Long questionTypeId, String questionTitle,
                                              int pageSize, int currentPage, String sortParam);

    boolean userFollowQuestion(Long questionId, Long userId);

    boolean userUnFollowQuestion(Long questionId, Long userId);

    boolean adminHiddenQuestion(Long questionId);

    boolean adminUnhiddenQuestion(Long questionId);

    boolean hasFollowQuestion(Long questionId, Long userId);

    boolean hasAnswerQuestion(Long questionId, Long userId);

    boolean getHidden(Long questionId);

    Page<Question> getUserFollowQuestions(Long userId, int currentPage, int pageSize);

    Page<Question> getUserQuestionByViews(Long userId, int currentPage, int pageSize);

    Page<Question> getUserQuestionByDateTime(Long userId, int currentPage, int pageSize);

    Question getQuestionByAnswerId(Long answerId);

    Long getUserQuestionCount(Long userId);
}
