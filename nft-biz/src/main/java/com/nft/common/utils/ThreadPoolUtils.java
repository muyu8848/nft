package com.nft.common.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ThreadPoolUtils {
	
	private static ScheduledThreadPoolExecutor blockChainAddrPool = new ScheduledThreadPoolExecutor(3);

	private static ScheduledThreadPoolExecutor syncChainPool = new ScheduledThreadPoolExecutor(10);

	private static ScheduledThreadPoolExecutor sendSmsPool = new ScheduledThreadPoolExecutor(4);
	
	public static ScheduledThreadPoolExecutor getBlockChainAddrPool() {
		return blockChainAddrPool;
	}

	public static ScheduledThreadPoolExecutor getSyncChainPool() {
		return syncChainPool;
	}

	public static ScheduledThreadPoolExecutor getSendSmsPool() {
		return sendSmsPool;
	}

}
