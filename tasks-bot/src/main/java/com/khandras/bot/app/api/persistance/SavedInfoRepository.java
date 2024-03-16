package com.khandras.bot.app.api.persistance;

import com.khandras.bot.domain.info.SavedInfo;

public interface SavedInfoRepository {
    void save(SavedInfo savedInfo);
}
