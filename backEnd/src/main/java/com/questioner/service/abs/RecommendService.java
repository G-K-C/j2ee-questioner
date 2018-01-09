package com.questioner.service.abs;

import java.util.List;

import com.questioner.entity.Question;
import com.questioner.viewmodel.Preference;
import org.apache.mahout.cf.taste.common.TasteException;

/**
 * @since 2017/11/7 下午8:59
 */
public interface RecommendService {
    void browseRecord(Long accountid, Long questionId);
    List<Question> recommend(Long accountid,int questionSize) throws TasteException;
    List<Preference> getPreferences(Long accountId,int preferenceSize) throws TasteException;
    void refreshRecommendSystem();
}
