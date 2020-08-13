package com.study.reatapi.restapi.config;

import com.study.reatapi.restapi.accounts.Account;
import com.study.reatapi.restapi.accounts.AccountRole;
import com.study.reatapi.restapi.accounts.AccountService;
import com.study.reatapi.restapi.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    public void 인증토큰_발급받는_테스트() throws Exception {
        String email = "justin@gmail.com";
        String password = "justin";

        accountService.saveAccount(Account.builder()
                .email(email)
                .password(password)
                .roles(Arrays.asList(AccountRole.ADMIN, AccountRole.USER))
                .build());

        String clientId = "myApp";
        String clientSecret = "pass";
        mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId, clientSecret))
                    .param("username", email)
                    .param("password", password)
                    .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}