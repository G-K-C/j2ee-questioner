package com.questioner.repository;

import com.questioner.entity.QuestionNotice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionNoticeRepository extends CrudRepository<QuestionNotice, Long> {
    List<QuestionNotice> getByAccount_IdAndHasRead(Long accountId, boolean hasRead);
}
