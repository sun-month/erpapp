package com.lingshi.erp.callback;

import com.lingshi.erp.bean.ServiceBus;

public interface RequestCallback {
	
	void success(ServiceBus bus);

	void error(String msg);
}
