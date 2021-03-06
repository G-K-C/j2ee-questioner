package com.questioner.controller;

import com.questioner.entity.QuestionType;
import com.questioner.jwt.JwtUser;
import com.questioner.service.abs.QuestionTypeService;
import com.questioner.util.ResJsonTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questionType")
public class QuestionTypeController {

    @Autowired
    private QuestionTypeService questionTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public ResJsonTemplate getAllQuestionType(){
        Iterable<QuestionType> questionTypeList = questionTypeService.getAllQuestionType();
        return new ResJsonTemplate<>("200", questionTypeList);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/hasFollow", method = RequestMethod.GET)
    public ResJsonTemplate hasFollow(@RequestParam("questionTypeId") Long questionTypeId) {
        JwtUser jwtUser =(JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwtUser.getId();
        return new ResJsonTemplate<>("200", questionTypeService.hasFollowQuestionType(questionTypeId,userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/modifyType", method = RequestMethod.PUT)
    public ResJsonTemplate modifyQuestionType(@RequestParam("questionTypeId") Long questionTypeId,
                                              @RequestParam("questionCourse") String questionCourse,
                                              @RequestParam("questionSubject") String questionSubject) {
        return new ResJsonTemplate<>("200", questionTypeService.modifyQuestionType(questionTypeId,questionCourse,questionSubject));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/addType", method = RequestMethod.POST)
    public ResJsonTemplate addQuestionType(@RequestParam("questionCourse") String questionCourse,
                                           @RequestParam("questionSubject") String questionSubject) {
        return new ResJsonTemplate<>("200", questionTypeService.addQuestionType(questionCourse,questionSubject));
    }

    @RequestMapping(value = "/follow", method = RequestMethod.PUT)
    public ResJsonTemplate followQuestionType(@RequestParam("questionTypeId") Long questionTypeId) {
        Long userId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return new ResJsonTemplate<>("200", questionTypeService.followQuestionType(questionTypeId, userId));
    }

    @RequestMapping(value = "/unFollow", method = RequestMethod.PUT)
    public ResJsonTemplate unFollowQuestionType(@RequestParam("questionTypeId") Long questionTypeId) {
        Long userId = ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return new ResJsonTemplate<>("200", questionTypeService.unFollowQuestionType(questionTypeId,userId));
    }

    @RequestMapping(value = "/getFollow/{userId}", method = RequestMethod.GET)
    public ResJsonTemplate getFollow(@PathVariable("userId") Long userId,
                                     @RequestParam(value = "currentPage",defaultValue = "0") int currentPage,
                                     @RequestParam(value = "pageSize", defaultValue = "0") int pageSize) {
        return new ResJsonTemplate<>("200", questionTypeService.getUserFollowQuestionType(userId, currentPage, pageSize));
    }
}
