package com.nft.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
	
	@GetMapping("/statistic-data")
	public String statisticData() {
		return "statistic-data";
	}
	
	@GetMapping("/withdraw-record")
	public String withdrawRecord() {
		return "withdraw-record";
	}
	
	@GetMapping("/compose-activity")
	public String composeActivity() {
		return "compose-activity";
	}
	
	@GetMapping("/pay-order")
	public String payOrder() {
		return "pay-order";
	}

	@GetMapping("/collection-give-record")
	public String collectionGiveRecord() {
		return "collection-give-record";
	}

	@GetMapping("/member-resale-collection")
	public String memberResaleCollection() {
		return "member-resale-collection";
	}

	@GetMapping("/member-hold-collection")
	public String memberHoldCollection() {
		return "member-hold-collection";
	}

	@GetMapping("/creator")
	public String creator() {
		return "creator";
	}
	
	@GetMapping("/mystery-box")
	public String mysteryBox() {
		return "mystery-box";
	}

	@GetMapping("/collection")
	public String collection() {
		return "collection";
	}

	@GetMapping("/member-data")
	public String memberData() {
		return "member-data";
	}

	@GetMapping("/sms-send-record")
	public String smsSendRecord() {
		return "sms-send-record";
	}

	@GetMapping("/notice")
	public String notice() {
		return "notice";
	}

	@GetMapping("/member-balance-change-log")
	public String memberBalanceChangeLog() {
		return "member-balance-change-log";
	}

	@GetMapping("/data-clean")
	public String dataClean() {
		return "data-clean";
	}

	@GetMapping("/statistical")
	public String statistical() {
		return "statistical";
	}

	@GetMapping("/setting")
	public String setting() {
		return "setting";
	}

	@GetMapping("/super-admin")
	public String superAdmin() {
		return "super-admin";
	}

	@GetMapping("/google-auth")
	public String googleAuth() {
		return "google-auth";
	}

	@GetMapping("/permission-fail")
	public String permissionFail() {
		return "permission-fail";
	}

	@GetMapping("/role-manage")
	public String roleManage() {
		return "role-manage";
	}

	@GetMapping("/menu-manage")
	public String menuManage() {
		return "menu-manage";
	}

	@GetMapping("/member")
	public String member() {
		return "member";
	}

	@GetMapping("/background-account")
	public String backgroundAccount() {
		return "background-account";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/dict-manage")
	public String dictManage() {
		return "dict-manage";
	}

	@GetMapping("/login-log")
	public String loginLog() {
		return "login-log";
	}

	@GetMapping("/oper-log")
	public String operLog() {
		return "oper-log";
	}

}
