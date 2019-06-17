package com.bj.scb.pay;

public class MyWXPayDomain implements IWXPayDomain {

	@Override
	public void report(String domain, long elapsedTimeMillis, Exception ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public DomainInfo getDomain(WXPayConfig config) {
		return new DomainInfo(config.getDomain(), true);
	}

}
