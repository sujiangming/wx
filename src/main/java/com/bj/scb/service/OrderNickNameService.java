package com.bj.scb.service;

import java.util.Map;

import com.bj.scb.pojo.OrderNickName;
import com.bj.scb.utils.PageList;

public interface OrderNickNameService {

	PageList<OrderNickName> selectAll(Map<String, Object> parameter);

	void saveObj(OrderNickName oNickName);

}
