package com.nft.chain.service;

public interface ChainAbstractService {

	String syncTransactionHash(String id);

	public String syncUniqueId(String id);

	public void transferArtwork(String id);

	public void marketBuyArtwork(String id);

	public String chainTransfer(String id);

	public void destroyArtwork(String id);

	public String mintArtwork(String id);

	public String createBlockChainAddr(String id);

	public String syncArtworkHash(String id);

	public String chainAddArtwork(String id);

}
