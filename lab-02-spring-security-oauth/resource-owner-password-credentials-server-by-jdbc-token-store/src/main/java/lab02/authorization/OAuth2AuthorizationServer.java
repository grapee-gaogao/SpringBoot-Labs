package lab02.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

// 授权服务器配置
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment env;

    // 用户认证
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()) // 设置 tokenStore
                // 告诉授权服务器 endpoints："发 token、查 token、撤token 全部用 JdbcTokenStore
                .authenticationManager(authenticationManager); // 设置 authenticationManager
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("clientapp").secret("112233") // Client 账号、密码。
//                .authorizedGrantTypes("password") // 密码模式
//                .scopes("read_userinfo", "read_contacts") // 可授权的 Scope
////                .and().withClient() // 可以继续配置新的 Client
//                ;

        // 加载 ClientDetails
        //client 信息从 oauth_client_details表里读
        //底层会创建一个 JdbcClientDetailsService，每次有请求带 client_id 进来都会查表。
        clients.jdbc(dataSource());
    }

    /**
     * 手动创建一个数据源
     */
    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.pass"));
        return dataSource;
    }

    /**
     * JdbcTokenStore 实现了 TokenStore接口
     *  所有"存 token / 查 token / 删token"的操作都落库
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource());
    }

}