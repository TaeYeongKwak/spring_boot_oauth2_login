package com.taeyeong.login.member.service;

import java.util.List;

import com.taeyeong.login.common.CommonResult;
import com.taeyeong.login.common.ListResult;
import com.taeyeong.login.common.SingleResult;

public interface ResponseService {
	
	public <T> SingleResult<T> getSingleResult(T data);
	public <T> ListResult<T> getListResult(List<T> list);
	public CommonResult getSuccessfulResult();
	public CommonResult getFailResult(int code, String message);

}
