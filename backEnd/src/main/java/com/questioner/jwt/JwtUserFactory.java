package com.questioner.jwt;

import com.questioner.entity.Account;
import com.questioner.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {
    private JwtUserFactory()
    {

    }

    public static JwtUser create(Account account)
    {
        return new JwtUser(
                account.getId(),
                account.getLoginUsername(),
                account.getPassword(),
                mapToGrantedAuthorities(account.getRoles()));
    }
    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> authorities)
    {
        return authorities.stream()
                .map(role->new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }









}
