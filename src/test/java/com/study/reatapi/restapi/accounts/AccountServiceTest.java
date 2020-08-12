package com.study.reatapi.restapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest  {

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 유저네임으로_유저를_찾는다() {
        // given
        String email = "justin@gmail.com";
        String password = "justin";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Arrays.asList(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountService.saveAccount(account);

        // when
        UserDetailsService accountService = this.accountService;
        UserDetails userDetails = accountService.loadUserByUsername(email);

        // then
        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }
}