package com.bj.scb.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.bj.scb.pojo.TimeCount;
import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.service.TimeCountService;
import com.bj.scb.service.TimeOrderCountService;
import com.bj.scb.utils.JdryTime;

@Controller
public class SpringQtz {

	@Resource
	TimeCountService timeCountService;// 时间段人数表dao

	@Resource
	TimeOrderCountService timeOrderCountService;// 用于显示当天还可以预约多少人的时间段人数表dao

	@Scheduled(cron = "0 0 0 * * ?")
	public void task() {
		// 思路：
		// 1、拿到TimeCount所有数据（关联：检车站名字）
		// 2、根据检车站名称、时间段来循环查询出TimeOrderCount符合条件的记录
		// 3、判断，如果满足上述2条件的，就执行更新操作，更新可以预约的人数；否则执行查询操作
		update();
	}

	public void update() {
		List<TimeCount> timeCountList = timeCountService.selectAll();
		if (timeCountList == null || timeCountList.size() == 0) {
			return;
		}

		String currentDate = JdryTime.getFormatDate(new Date().getTime(), "yyyy-MM-dd");

		for (TimeCount timeCount : timeCountList) {
			String checkStationName = timeCount.getCheckStationName();
			String orderTime = timeCount.getOrderTime();
			int orderCount = timeCount.getOrderCount();

			TimeOrderCount timeOrderCount = timeOrderCountService.queryByConditaion(orderTime, currentDate,
					checkStationName);
			if (null == timeOrderCount) {
				timeOrderCount = new TimeOrderCount();
				timeOrderCount.setCheckStationName(checkStationName);
				timeOrderCount.setOrderTime(orderTime);
				timeOrderCount.setTimeCountId(timeCount.getId());
				timeOrderCount.setOrderStartTime(timeCount.getOrderStartTime());
			}

			timeOrderCount.setOrderCount(String.valueOf(orderCount));
			timeOrderCount.setOrderDate(currentDate);
			timeOrderCount.setUpdateTime(JdryTime.getFullTimeBySec(new Date().getTime()));
			// 保存入库
			timeOrderCountService.saveOrUpdate(timeOrderCount);
		}
	}

}
