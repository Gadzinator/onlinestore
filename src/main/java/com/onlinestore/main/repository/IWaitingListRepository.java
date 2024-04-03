package com.onlinestore.main.repository;

import com.onlinestore.main.domain.entity.WaitingList;

import java.util.Optional;

public interface IWaitingListRepository {
    void add(WaitingList waitingList);

    Optional<WaitingList> indById(long id);

    void update(long id, WaitingList waitingListUpdate);

    void delete(long id);
}
