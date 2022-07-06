package com.nft.common.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadPoolUtils {

	private static ScheduledThreadPoolExecutor unreadNoticePool = new ScheduledThreadPoolExecutor(2);
	
	private static ScheduledThreadPoolExecutor riskPool = new ScheduledThreadPoolExecutor(6);
	
	private static ScheduledThreadPoolExecutor transferPool = new ScheduledThreadPoolExecutor(8);
	
	private static ScheduledThreadPoolExecutor sendSmsPool = new ScheduledThreadPoolExecutor(4);

	public static ScheduledThreadPoolExecutor getUnreadNoticePool() {
		return unreadNoticePool;
	}
	
	public static ScheduledThreadPoolExecutor getRiskPool() {
		return riskPool;
	}
	
	public static ScheduledThreadPoolExecutor getTransferPool() {
		return transferPool;
	}
	
	public static ScheduledThreadPoolExecutor getSendSmsPool() {
		return sendSmsPool;
	}

}
