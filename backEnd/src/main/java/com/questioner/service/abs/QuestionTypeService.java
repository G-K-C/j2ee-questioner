package com.questioner.service.abs;

import com.questioner.entity.QuestionType;
import org.springframework.data.domain.Page;

public interface QuestionTypeService {
    Iterable<QuestionType> getAllQuestionType();

    QuestionType getQuestionType(Long questionTypeId);

    boolean hasFollowQuestionType(Long questionTypeId, Long userId);

    boolean followQuestionType(Long questionTypeId, Long userId);

    boolean unFollowQuestionType(Long questionTypeId, Long userId);

    boolean modifyQuestionType(Long questionTypeId, String questionCourse, String questionSubject);

    boolean addQuestionType(String questionCourse, String questionSubject);

    Page<QuestionType> getUserFollowQuestionType(Long userId, int currentPage, int pageSize);
}
