package com.prj.springai.piotrblog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prj.springai.piotrblog.model.Share;

public interface WalletRepository  extends JpaRepository<Share, Long>{

}
