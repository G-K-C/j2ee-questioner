package com.questioner.repository;

import com.questioner.entity.AnswerComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerCommentRepository extends CrudRepository<AnswerComment, Long>{
    Long countByAnswerId(Long answerId);

    List<AnswerComment> findByAnswerId(Long answerId);
}
