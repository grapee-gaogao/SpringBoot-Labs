package lab01.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

// 授权服务器配置
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    // 用户认证
    /**
     * 密码模式必须有 AuthenticationManager ——
     *   因为授权服务器拿到 username/password 后，
     *   需要走 Spring Security 标准认证流程
     *   去校验"这个用户密码对不对"。
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 把 AuthenticationManager 注入到 /oauth/token 端点
     * 没有这一行，密码模式会直接报错：Unsupported
     * grant type: password。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("clientapp").secret("112233") // Client 账号、密码。
                .authorizedGrantTypes("password") // 密码模式
                .scopes("read_userinfo", "read_contacts") // 可授权的 Scope
//                .and().withClient() // 可以继续配置新的 Client
                ;
    }

}