package com.taeyeong.login.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taeyeong.login.common.CommonResult;
import com.taeyeong.login.common.ListResult;
import com.taeyeong.login.common.SingleResult;

@Service
public class ResponseServiceImpl implements ResponseService{
	
	public enum CommonResponse {
        SUCCESS(0, "성공하였습니다.");

        int code;
        String message;

        CommonResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
	
	@Override
	public <T> SingleResult<T> getSingleResult(T data) {
		SingleResult<T> result = new SingleResult<T>();
		result.setData(data);
		this.setSuccessResult(result);
		return result;
	}

	@Override
	public <T> ListResult<T> getListResult(List<T> list) {
		ListResult<T> result = new ListResult<T>();
		result.setList(list);
		this.setSuccessResult(result);
		result.setSuccess(true);
		return result;
	}

	@Override
	public CommonResult getSuccessfulResult() {
		CommonResult result = new CommonResult();
		this.setSuccessResult(result);
		return result;
	}

	@Override
	public CommonResult getFailResult(int code, String message) {
		CommonResult result = new CommonResult();
		result.setSuccess(false);
		result.setCode(code);
		result.setMessage(message);
		return result;
	}
	
	private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());
    }
	
}
