package lab2.authorization.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  OAuth2 协议在 RFC 7009
 *   (https://www.rfc-editor.org/rfc/rfc7009) 里专门定义了
 *   Token Revocation 端点。
 *   Spring Security OAuth2 老版没自带
 *   所以本模块自己手写一个
 *   /api/access_token/revoke 和
 *   /api/refresh_token/revoke。
 */
@RestController
public class TokenController {

    @Autowired
    /**
     * - ConsumerTokenServices：Spring Security OAuth2 提供的接口
     *   专门用于"消费/作废token"
     *   revokeToken(String) 会从 TokenStore中删掉这个 access_token
     *   并连带删掉它关联的refresh_token。
     */
    private ConsumerTokenServices tokenServices;

    @Autowired(required = false)
    /**
     * TokenStore：token 的存储抽象，默认实现是InMemoryTokenStore
     * InMemoryTokenStore = 把 OAuth2 的令牌（token）存在「服务器当前运行的内存里」
     * 服务器一关 → 所有令牌全部消失
     * 服务器重启 → 所有用户全部掉线
     * removeRefreshToken 单独删refresh_token
     */
    private TokenStore tokenStore;

    /**
     * 删除access_token和关联的refresh_token
     */
    @RequestMapping(method = RequestMethod.POST, value = "api/access_token/revoke")
    public String revokeToken(@RequestParam("token") String token) {
        tokenServices.revokeToken(token);
        return token;
    }

    /**
     * 只删除refresh_token
     */
    @RequestMapping(method = RequestMethod.POST, value = "api/refresh_token/revoke")
    public String revokeRefreshToken(@RequestParam("token") String token) {
        tokenStore.removeRefreshToken(new DefaultOAuth2RefreshToken(token));
        return token; //返回 token 只是为了方便调试，看到回显说明操作成功
    }

}