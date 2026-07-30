package com.prj.springai.piotrblog.functions.wallet;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.prj.springai.piotrblog.services.WalletService;

public class WalletFunction implements Supplier<WalletResponse>{
	private static final Logger log = LogManager.getLogger(WalletFunction.class);

	private WalletService walletService;
	
	public WalletFunction(WalletService walletService) {
		this.walletService = walletService;
	}

	@Override
	public WalletResponse get() {
		log.info("[WalletFunction] Function triggered...");
		return new WalletResponse(this.walletService.findAllShares());
	}

}
