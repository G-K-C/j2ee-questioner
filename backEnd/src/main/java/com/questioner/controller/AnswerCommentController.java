package com.questioner.controller;

import com.questioner.entity.Account;
import com.questioner.entity.Answer;
import com.questioner.entity.AnswerComment;
import com.questioner.jwt.JwtUser;
import com.questioner.service.abs.AnswerCommentService;
import com.questioner.util.ResJsonTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answerComment")
public class AnswerCommentController {
    @Autowired
    private AnswerCommentService answerCommentService;

    @RequestMapping(value = "/{answerId}", method = RequestMethod.POST)
    public ResJsonTemplate sendAnswerComment(@RequestBody AnswerComment answerComment,
                                             @PathVariable("answerId") Long answerId){
        Long userId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Answer answer = new Answer();
        answer.setId(answerId);
        answerComment.setAnswer(answer);
        Account account = new Account();
        account.setId(userId);
        answerComment.setCommenter(account);
        return new ResJsonTemplate<>("201", answerCommentService.saveAnswerComment(answerComment));
    }

    @RequestMapping(value = "/{answerId}", method = RequestMethod.GET)
    public ResJsonTemplate getCommentsOfAnswer(@PathVariable("answerId") Long answerId){
        return new ResJsonTemplate<>("200", answerCommentService.getAllCommentsOfAnswer(answerId));
    }

    @RequestMapping(value = "/getCommentCount/{answerId}", method = RequestMethod.GET)
    public ResJsonTemplate getCommentCountOfAnswer(@PathVariable("answerId") Long answerId){
        return new ResJsonTemplate<>("201", answerCommentService.getCommentCountOfAnswer(answerId));
    }


}
