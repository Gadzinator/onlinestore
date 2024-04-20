package com.onlinestore.main.repository.impl;

import com.onlinestore.main.domain.entity.WaitingList;
import com.onlinestore.main.repository.IWaitingListRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class WaitingListRepository implements IWaitingListRepository {

	private final List<WaitingList> waitingLists = new ArrayList<>();

	@Override
	public void add(WaitingList waitingList) {
		waitingLists.add(waitingList);
	}

	@Override
	public Optional<WaitingList> indById(long id) {
		return waitingLists.stream()
				.filter(waitingList -> waitingList.getId() == id)
				.findFirst();
	}

	@Override
	public void update(long id, WaitingList waitingListUpdate) {
		for (int i = 0; i < waitingLists.size(); i++) {
			WaitingList waitingList = waitingLists.get(i);
			if (waitingList.getId() == id) {
				waitingLists.set(i, waitingListUpdate);
			}
		}
	}

	@Override
	public void delete(long id) {
		waitingLists.removeIf(waitingList -> waitingList.getId() == id);
	}
}
