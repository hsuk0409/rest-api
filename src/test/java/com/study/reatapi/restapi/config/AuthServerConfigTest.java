package com.study.reatapi.restapi.config;

import com.study.reatapi.restapi.accounts.Account;
import com.study.reatapi.restapi.accounts.AccountRole;
import com.study.reatapi.restapi.accounts.AccountService;
import com.study.reatapi.restapi.common.AppProperties;
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

    @Autowired
    AppProperties appProperties;

    @Test
    public void 인증토큰_발급받는_테스트() throws Exception {
        Account account = accountService.saveAccount(Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Arrays.asList(AccountRole.ADMIN, AccountRole.USER))
                .build());

        mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                    .param("username", account.getEmail())
                    .param("password", account.getPassword())
                    .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}