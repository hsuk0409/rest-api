package com.study.reatapi.restapi.accounts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest  {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void 유저네임으로_유저를_찾는다() {
        // given
        String email = "justin@gmail.com";
        String password = "justin";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Sets.newSet(AccountRole.ADMIN, AccountRole.USER))
                .build();
        Account savedAccount = this.accountRepository.save(account);

        // when
        UserDetailsService accountService = this.accountService;
        UserDetails userDetails = accountService.loadUserByUsername(email);

        // then
        assertThat(userDetails.getPassword()).isEqualTo(savedAccount.getPassword());
    }
}