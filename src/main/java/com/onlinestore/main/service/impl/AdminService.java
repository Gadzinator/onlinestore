package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public void changeUserRole(String userName, String newRole) {
		final User user = userRepository.findByName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("There is no user with that name " + userName));
		user.setRole(Role.valueOf(newRole));
		userRepository.add(user);
	}
}
