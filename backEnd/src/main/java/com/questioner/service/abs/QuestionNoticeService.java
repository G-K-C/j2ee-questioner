package com.questioner.service.abs;

import com.questioner.entity.Answer;
import com.questioner.entity.QuestionNotice;

import java.util.List;

public interface QuestionNoticeService {
     void createNoticeAfterAnswerQuestion(Answer answer);

     void createNoticeAfterHideQuestion(Long questionId);

     void createNoticeAfterHideAnswer(Answer answer);

     void createNoticeAfterAcceptAnswer(Answer answer);

     void createNoticeAfterUnhideQuestion(Long questionId);

     void createNoticeAfterUnhideAnswer(Answer answer);

     void markAsHasRead(Long questionNoticeId);

     List<QuestionNotice> getUserNotReadNotice(Long accountId);
}
