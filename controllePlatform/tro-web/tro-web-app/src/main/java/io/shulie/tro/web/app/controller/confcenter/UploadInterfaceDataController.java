//package io.shulie.tro.web.app.controller.confcenter;
//
//import com.pamirs.tro.common.ResponseError;
//import com.pamirs.tro.common.ResponseOk;
//import com.pamirs.tro.common.constant.TROErrorEnum;
//import com.pamirs.tro.common.exception.TROModuleException;
//import com.pamirs.tro.entity.domain.vo.TUploadInterfaceVo;
//import com.pamirs.tro.entity.domain.vo.TUploadNeedVo;
//import io.shulie.tro.web.app.constant.APIUrls;
//import io.shulie.tro.web.app.service.UploadInterfaceService;
//import io.swagger.annotations.Api;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 说明: 二级链路管理接口
// *
// * @author shulie
// * @version v1.0
// * @create 2018/6/20 14:50
// */
//@Api(tags = "二级链路管理接口")
//@RestController
//@RequestMapping(APIUrls.TRO_API_URL)
//public class UploadInterfaceDataController {
//
//    private final Logger LOGGER = LoggerFactory.getLogger(UploadInterfaceDataController.class);
//
//    @Autowired
//    private UploadInterfaceService uploadInterfaceService;
//
//    /**
//     * 判断是否需要上传
//     *
//     * @param uploadNeedVo appName与数量
//     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
//     */
//    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_INTERFACE_NEED_UPLOAD_URL,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<Object> judgeNeedUpload(@RequestBody TUploadNeedVo uploadNeedVo) {
//        try {
//            return ResponseOk.create(uploadInterfaceService.executeNeedUploadInterface(uploadNeedVo));
//        } catch (TROModuleException e) {
//            LOGGER.error("UploadInterfaceDataController.judgeNeedUpload 查询是否需要上传异常{}", e);
//            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
//        } catch (Exception e) {
//            LOGGER.error("UploadInterfaceDataController.judgeNeedUpload 查询是否需要上传异常{}", e);
//            return ResponseError.create(TROErrorEnum.CONFCENTER_INTERFACE_NEED_UPLOAD_EXCEPTION.getErrorCode(),
//                TROErrorEnum.CONFCENTER_INTERFACE_NEED_UPLOAD_EXCEPTION.getErrorMessage());
//        }
//    }
//
//    /**
//     * 判断是否需要上传
//     *
//     * @param TUploadInterfaceVo appName与接口信息
//     * @return 成功, 则返回成功信息, 失败则返回错误编码和错误信息
//     */
//    @PostMapping(value = APIUrls.API_TRO_CONFCENTER_INTERFACE_UPLOAD_URL,
//        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<Object> judgeNeedUpload(@RequestBody TUploadInterfaceVo TUploadInterfaceVo) {
//        try {
//            return ResponseOk.create(uploadInterfaceService.saveUploadInterfaceData(TUploadInterfaceVo));
//        } catch (TROModuleException e) {
//            LOGGER.error("UploadInterfaceDataController.judgeNeedUpload 查询是否需要上传异常{}", e);
//            return ResponseError.create(e.getErrorCode(), e.getErrorMessage());
//        } catch (Exception e) {
//            LOGGER.error("UploadInterfaceDataController.judgeNeedUpload 查询是否需要上传异常{}", e);
//            return ResponseError.create(TROErrorEnum.CONFCENTER_INTERFACE_UPLOAD_EXCEPTION.getErrorCode(),
//                TROErrorEnum.CONFCENTER_INTERFACE_NEED_UPLOAD_EXCEPTION.getErrorMessage());
//        }
//    }
//}
