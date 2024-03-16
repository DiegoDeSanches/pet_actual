package com.khandras.bot.adapter.persistence.savedinfo;

import com.khandras.bot.app.api.persistance.SavedInfoRepository;
import com.khandras.bot.domain.info.SavedInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SavedInfoRepositoryAdapter implements SavedInfoRepository {
    private final SavedInfoCrudRepository repo;

    @Override
    public void save(SavedInfo savedInfo) {
        repo.save(savedInfo);
    }
}
