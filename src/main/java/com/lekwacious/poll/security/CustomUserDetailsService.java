package com.lekwacious.poll.security;

import com.lekwacious.poll.data.models.User;
import com.lekwacious.poll.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
   @Autowired
    UserRepository userRepository;

   @Transactional
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
       // Let people login with either username or email

       User user  = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
               .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email : ".concat(usernameOrEmail)));

       return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Integer id){
       User user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("User notfound with iid: "+ id));
    return UserPrincipal.create(user);
   }
}
