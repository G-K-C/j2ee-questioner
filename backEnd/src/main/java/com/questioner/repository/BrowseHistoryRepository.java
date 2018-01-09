package com.questioner.repository;

import java.util.List;

import com.questioner.entity.BrowseHistory;
import org.springframework.data.repository.CrudRepository;

/**
 * @since 2017/11/6 下午9:16
 */
public interface BrowseHistoryRepository extends CrudRepository<BrowseHistory, Long> {
    @Override
    BrowseHistory save(BrowseHistory browseHistory);
    BrowseHistory findByUseridAndItemid(Long userid,Long itemid);
    List<BrowseHistory> findByUserid(Long userid);
}
