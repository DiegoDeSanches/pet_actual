package com.khandras.bot.adapter.persistence.savedinfo;

import com.khandras.bot.domain.info.SavedInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SavedInfoCrudRepository extends CrudRepository<SavedInfo, String> {
    List<SavedInfo> findAll();
}
