package com.waiterxiaoyy.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.waiterxiaoyy.common.exception.CaptchaException;
import com.waiterxiaoyy.common.lang.Const;
import com.waiterxiaoyy.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/1/18 19:31
 * @Version 1.0
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    LoginFailureHandler loginFailureHandler;


    /**
     * 登录拦截检验验证码
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURI(); // 获取请求路径

        // 如果是登录接口，请拦截进行操作
        if(url.equals("/login") && httpServletRequest.getMethod().equals("POST")) {
            // 校验验证码
            try {
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 如果不正确，就跳转到认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 将验证码与Redis中存储进行比对
     * @param httpServletRequest
     */
    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code"); // 从请求体中取出验证码
        String key = httpServletRequest.getParameter("token"); // 从请求体中取出key值

        if(StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 失效
        redisUtil.hdel(Const.CAPTCHA_KEY, key);

    }
}
