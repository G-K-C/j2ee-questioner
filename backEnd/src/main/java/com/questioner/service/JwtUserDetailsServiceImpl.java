package com.questioner.service;

import com.questioner.entity.Account;
import com.questioner.jwt.JwtUserFactory;
import com.questioner.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String loginUsername) throws UsernameNotFoundException {
        Account account = accountRepository.findByLoginUsername(loginUsername);
        if(account == null)
        {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'",
                    loginUsername));
        }
        else
        {
            return JwtUserFactory.create(account);
        }
    }
}
