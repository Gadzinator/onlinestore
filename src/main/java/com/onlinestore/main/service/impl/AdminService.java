package com.onlinestore.main.service.impl;

import com.onlinestore.main.domain.entity.Role;
import com.onlinestore.main.domain.entity.User;
import com.onlinestore.main.repository.impl.UserRepository;
import com.onlinestore.main.service.IAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
public class AdminService implements IAdminService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public void changeUserRole(String userName, String newRole) {
		log.info("Starting work on changing a user's role: " + userName + " to the role of " + newRole);

		final User user = userRepository.findByName(userName)
				.orElseThrow(() -> new UsernameNotFoundException("There is no user with that name " + userName));
		user.setRole(Role.valueOf(newRole));
		userRepository.add(user);

		log.info("Changed user role: " + user + " to role " + user.getRole());
	}
}
