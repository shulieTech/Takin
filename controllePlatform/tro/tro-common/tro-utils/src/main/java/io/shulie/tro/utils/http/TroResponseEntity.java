package io.shulie.tro.utils.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.type.TypeReference;
import io.shulie.tro.utils.json.JsonHelper;
import lombok.Data;
import okhttp3.Response;

/**
 * @author 无涯
 * @Package io.shulie.tro.utils.http
 * @date 2020/11/4 10:21 上午
 */
@Data
public class TroResponseEntity<T> {

    private static final Integer OK_CODE = 200;
    private static final Integer BAD_CODE = 400;

    private Boolean success;
    private Integer httpStatus;
    private T body;
    private String errorMsg;

    public TroResponseEntity() {
        this.success = true;
        this.httpStatus = OK_CODE;
        this.body = null;
        this.errorMsg = "";
    }

    public static <T> TroResponseEntity<T> create(Response response, Class<T> clazz, TypeReference<T> typeRef) {
        TroResponseEntity<T> troResponseEntity = new TroResponseEntity<>();

        String responseStr = null;
        String errorMsg = null;
        try {
            if (response.body() != null) {
                responseStr = new String(response.body().string().getBytes(
                    StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            errorMsg = e.getMessage();
        }
        if (!response.isSuccessful()) {
            troResponseEntity.setHttpStatus(response.code());
            troResponseEntity.setSuccess(false);
            troResponseEntity.setBody(null);
            troResponseEntity.setErrorMsg(responseStr != null ? responseStr : errorMsg);
            return troResponseEntity;
        }
        if (typeRef != null && responseStr != null) {
            troResponseEntity.setBody(JsonHelper.string2Obj(responseStr, typeRef));
            return troResponseEntity;
        }
        if (clazz != null && responseStr != null) {
            troResponseEntity.setBody(JsonHelper.string2Obj(responseStr, clazz));
            return troResponseEntity;
        }
        throw new IllegalArgumentException("clazz，typeRef 必须有一个有值");
    }

    public static <T> TroResponseEntity<T> error(String s) {
        TroResponseEntity<T> troResponseEntity = new TroResponseEntity<>();
        troResponseEntity.setHttpStatus(BAD_CODE);
        troResponseEntity.setErrorMsg(s);
        return troResponseEntity;
    }

}
