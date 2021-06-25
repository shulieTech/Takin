/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.app.controller.verificationcode;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.common.util.verificationcode.VerificationCodeUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.controller.verificationcode
 * @date 2021/3/31 6:58 下午
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Slf4j
public class VerificationCodeController {

    @ApiOperation("验证码获取")
    @GetMapping(value = "verification/code")
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) {
        BufferedImage img= VerificationCodeUtil.getImage();
        try {
            //验证码
            String code = VerificationCodeUtil.VERIFICATION_CODE();
            //后端保存验证码code和sessionId
            request.getSession().setAttribute(VerificationCodeUtil.VERIFICATION,code);
            // 获取上一个
            String accessToken = request.getHeader("Access-Token");
            VerificationCodeUtil.delSession(accessToken);
            VerificationCodeUtil.addSession(request.getSession());
            response.setHeader("Access-Control-Expose-Headers", "Access-Token");
            //将sessionId通过响应头传回客户端
            response.setHeader("Access-Token", request.getSession().getId());
            // 禁止图像缓存
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", -1);
            response.setContentType("image/jpeg");
            VerificationCodeUtil.saveImage(img, response.getOutputStream());
        } catch (IOException e) {
            log.error("生成图片验证码异常", e);
        }
    }

}
