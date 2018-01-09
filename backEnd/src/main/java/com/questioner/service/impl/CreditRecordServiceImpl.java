package com.questioner.service.impl;

import java.util.List;

import com.questioner.entity.CreditRecord;
import com.questioner.repository.CreditRecordRepository;
import com.questioner.service.abs.CreditRecordService;
import com.questioner.util.PageableBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @since 2017/10/23 上午10:49
 */
@Service
public class CreditRecordServiceImpl implements CreditRecordService {
    @Autowired
    private  CreditRecordRepository creditRecordRepository;
    @Override
    public CreditRecord save(CreditRecord creditRecord) {
        return creditRecordRepository.save(creditRecord);
    }

    @Override
    public Page<CreditRecord> getUserCreditRecordPageable(Long userId, int currentPage, int pageSize) {
        Pageable pageable = new PageableBuilder().setPageSize(pageSize)
                .setCurrentPage(currentPage).setSortParam("id")
                .setDirection(Sort.Direction.DESC).buildPage();
        return creditRecordRepository.getUserCreditRecordsPageable(userId, pageable);
    }

}
