package cn.iocoder.springboot.labs.lab01.authorization;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

// 授权服务器配置
@Configuration
@EnableAuthorizationServer // 告诉spring：把这个应用变成一个 OAuth2 服务台
/**
 * 启用授权服务器，自动注册
 * /oauth/token、/oauth/check_token 等端点
 */
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()  // 把朋友的信息存在内存里（重启就没）
                .withClient("clientapp").secret("112233") // 朋友的账号、密码。
                .redirectUris("http://localhost:9001/callback") // 授权后，服务台把"临时授权单"送到这个地址，配置回调地址，选填。
                .authorizedGrantTypes("authorization_code") // 只允许授权码模式
                .scopes("read_userinfo", "read_contacts") // 朋友能申请的权限范围
//                .and().withClient() // 可以继续配置新的 Client
                ;
    }

}