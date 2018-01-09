package com.questioner.service.abs;


import com.questioner.entity.CreditRecord;
import org.springframework.data.domain.Page;

/**
 * @since 2017/10/23 上午10:49
 */
public interface CreditRecordService {
    CreditRecord save(CreditRecord creditRecord);

    Page<CreditRecord> getUserCreditRecordPageable(Long userId, int currentPage, int pageSize);
}
