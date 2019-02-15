package com.cloud.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //使用内存存储
        clients
        		.inMemory()
                //分配客户端账号
                .withClient("client1")
                .secret(passwordEncoder.encode("client1-secret"))
                //支持的授权类型
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .redirectUris("http://localhost:9001/client1/login")
                .scopes("read")
                //token有效时长
                .accessTokenValiditySeconds(1200)
                //refreshToken有效时长
                .refreshTokenValiditySeconds(50000)
                .and()
                .withClient("resource-server")
                .secret(passwordEncoder.encode("resource-secret"))
                .authorizedGrantTypes("authorization_code","refresh_token")
                .scopes("all")
                .and()
                //分配客户端账号
                .withClient("test")
                .secret(passwordEncoder.encode("test"))
                //支持的授权类型
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("all");
                
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //设置签名密钥
        jwtAccessTokenConverter.setSigningKey("demo");
        return jwtAccessTokenConverter;
    }

    //使用JWT作为token
    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jwtTokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .reuseRefreshTokens(true)
                //配置以生效password模式
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()");
    }
}