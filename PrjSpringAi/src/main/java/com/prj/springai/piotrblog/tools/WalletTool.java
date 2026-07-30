package com.prj.springai.piotrblog.tools;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.prj.springai.piotrblog.model.Share;
import com.prj.springai.piotrblog.services.WalletService;

@Component
public class WalletTool {
	private static final Logger log = LogManager.getLogger(WalletTool.class);

	private WalletService walletService;
	
	public WalletTool(WalletService walletService) {
		this.walletService = walletService;
	}
	
	@Tool(description = "Fetch shares in my wallet. Call this tool only once.")
	public List<Share> getNumberOfShares(){
		log.info("[getNumberOfShares] Tool triggered...");
		return walletService.findAllShares();
	}
}
