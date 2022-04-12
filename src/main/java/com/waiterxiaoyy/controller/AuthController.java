package com.waiterxiaoyy.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.waiterxiaoyy.common.lang.Const;
import com.waiterxiaoyy.common.lang.Result;
import com.waiterxiaoyy.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

/**
 * 功能描述：验证控制器
 *
 * @Author WaiterXiaoYY
 * @Date 2022/1/17 13:51
 * @Version 1.0
 */

@RestController
public class AuthController extends BaseController {


    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        String key = UUID.randomUUID().toString(); // 设置一个随机key值
        String code = producer.createText(); // 使用验证码工具生成一个code

        BufferedImage image = producer.createImage(code); // 将code转成图片字节流

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "jpg", outputStream); // 将图片字节流输出为jpg格式的图片

        BASE64Encoder encoder = new BASE64Encoder(); // 将图片转成base64编码
        String str = "data:image/jpeg;base64,";

        String base64Img = str +encoder.encode(outputStream.toByteArray()); // 将图片转成base64编码

        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120 ); // 在Redis缓存中存入key-code

        return Result.succ(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", base64Img)
                        .build()

        );
    }
    /**
     * 获取用户信息接口
     * @param principal
     * @return
     */
    @GetMapping("/sys/userInfo")
    public Result userInfo(Principal principal) {

        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        return Result.succ(sysUser);
    }

    /**
     * 用户更新自己的信息
     * @param sysUser
     * @return
     */
    @PostMapping("/sys/updateme")
    public Result userInfo(@Validated @RequestBody SysUser sysUser) {

        sysUserService.updateById(sysUser);

        return Result.succ(sysUser);
    }
}
