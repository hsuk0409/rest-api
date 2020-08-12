package com.study.reatapi.restapi.config;

import com.study.reatapi.restapi.accounts.Account;
import com.study.reatapi.restapi.accounts.AccountRole;
import com.study.reatapi.restapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) {
                Account.builder()
                        .email("justin@email.com")
                        .password("justin")
                        .roles(Arrays.asList(AccountRole.ADMIN, AccountRole.USER))
                        .build();
            }
        };
    }
}
