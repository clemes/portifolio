package com.prj.springai.piotrblog.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.prj.springai.piotrblog.model.Share;
import com.prj.springai.piotrblog.repositories.WalletRepository;

@Service
public class WalletService {

	private static final Logger log = LogManager.getLogger(WalletService.class);

	private WalletRepository walletRepository;
	
	public WalletService(WalletRepository walletRepository) {
		this.walletRepository = walletRepository;
	}

	public List<Share> findAllShares() {
		List<Share> shares = walletRepository.findAll();
		log.info("[WalletService] Shares found: \n{}", shares);
		return shares;
	}

}
