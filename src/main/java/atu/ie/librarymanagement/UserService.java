package atu.ie.librarymanagement;

import atu.ie.librarymanagement.entity.User;
import atu.ie.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setBorrowingHistory(updatedUser.getBorrowingHistory());
        return userRepository.save(user);
    }
}
