package com.questioner.controller;

import com.questioner.entity.QuestionNotice;
import com.questioner.jwt.JwtUser;
import com.questioner.service.abs.QuestionNoticeService;
import com.questioner.util.ResJsonTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/notice")
public class QuestionNoticeController {

    @Autowired
    private QuestionNoticeService questionNoticeService;

    @RequestMapping(value = "/readNotice/{noticeId}", method = RequestMethod.PUT)
    public ResJsonTemplate readNotice(@PathVariable("noticeId") Long noticeId) {
        questionNoticeService.markAsHasRead(noticeId);
        return new ResJsonTemplate<>("200", true);
    }

    @RequestMapping(value = "/getUnreadNotice", method = RequestMethod.GET)
    public ResJsonTemplate getUnreadNotice() {
        Long userId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<QuestionNotice> questionNoticeList = questionNoticeService.getUserNotReadNotice(userId);

        return new ResJsonTemplate<>("200", questionNoticeList);
    }
}
