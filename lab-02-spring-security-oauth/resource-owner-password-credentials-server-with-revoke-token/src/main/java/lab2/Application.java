package lab2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这个模块解决一个核心痛点：OAuth2 默认的 access_token 无法主动作废
 *
 *   为什么？
 *   因为 access_token 有效期内，
 *   资源服务器只看 token 是否在 TokenStore 里有记录、是否过期。
 *   除非过期或 TokenStore 被清，否则token 一直能用。
 *   但实际业务里有大量"主动失效"场景：
 *
 *   - 用户点退出登录 → 当前 token 必须立即失效
 *   - 用户改密码 → 旧 token 应该全部失效
 *   - 用户踢下线、封号 → 后台需要立即吊销 token
 *   - 检测到异常登录风险 → 主动失效该 token
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}