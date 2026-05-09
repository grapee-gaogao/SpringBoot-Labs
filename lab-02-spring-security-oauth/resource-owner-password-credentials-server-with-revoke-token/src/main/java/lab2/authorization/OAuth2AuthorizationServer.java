package lab2.authorization;

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
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    /**
     * endpoints.reuseRefreshTokens(true);   默认：续期时复用同一个 refresh_token
     * endpoints.reuseRefreshTokens(false);  滚动刷新：每次续期返回新的 refresh_token，旧的失效
     *
     * - 复用模式（默认）：refresh_token一直是同一个，直到自己过期。简单。
     * - 滚动模式：每次刷新发一个新的 refresh_token，老的立即作废。安全性更高（OAuth2.1 推荐做法）。
     * 如果攻击者偷了 refresh_token 但用户也在用，会出现"双方都来续期"，授权服务器能检测到异常。
     *
     *   本模块用的是默认值，没改 reuse 行为。
     */
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("clientapp").secret("112233") // Client 账号、密码。
                .authorizedGrantTypes("password", "refresh_token") // 密码模式
                .scopes("read_userinfo", "read_contacts") // 可授权的 Scope
                .refreshTokenValiditySeconds(1200) // 1200 秒过期
        /**
         * 不写默认是 30 天（60 * 60 * 24 * 30）
         * access_token 不写默认是 12 小时（43199 秒）
         */
//                .and().withClient() // 可以继续配置新的 Client
                ;
    }

}