package com.questioner.controller;

import com.questioner.entity.CreditRecord;
import com.questioner.entity.Question;
import com.questioner.service.abs.CreditRecordService;
import com.questioner.service.abs.QuestionService;
import com.questioner.util.ResJsonTemplate;
import com.questioner.viewmodel.ReputationRecord;
import com.questioner.viewmodel.ReputationRecordPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/creditRecord")
public class CreditRecordController {

    @Autowired
    private CreditRecordService creditRecordService;

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResJsonTemplate getUserCreditRecord(@PathVariable("userId") Long userId,
                                               @RequestParam("currentPage") int currentPage,
                                               @RequestParam("pageSize") int pageSize) {
        Page<CreditRecord> creditRecordPage = creditRecordService.getUserCreditRecordPageable(userId, currentPage, pageSize);
        ReputationRecordPage reputationRecordPage = constructReputationPage(creditRecordPage, questionService);
        return new ResJsonTemplate<>("200", reputationRecordPage);
    }
    private ReputationRecordPage constructReputationPage(Page<CreditRecord> creditRecords, QuestionService questionService) {
        ReputationRecordPage reputationRecordPage = new ReputationRecordPage();
        reputationRecordPage.setTotalNumber(creditRecords.getTotalElements());
        List<CreditRecord> creditRecordList = creditRecords.getContent();
        List<ReputationRecord> reputationRecordList = new ArrayList<>();
        for (CreditRecord creditRecord: creditRecordList) {
            Long answerId = creditRecord.getAnswer().getId();
            Question question = questionService.getQuestionByAnswerId(answerId);
            if (question != null)
            {
                ReputationRecord reputationRecord = new ReputationRecord();
                reputationRecord.setAnswerId(answerId).setFeedbackDateTime(creditRecord.getDatetime())
                        .setQuestionId(question.getId()).setQuestionTitle(question.getQuestionTitle())
                        .setReputationType(creditRecord.getType());
                reputationRecordList.add(reputationRecord);
            }
        }
        reputationRecordPage.setReputationRecords(reputationRecordList);
        return reputationRecordPage;
    }
}
