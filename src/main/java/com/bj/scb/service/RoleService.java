package com.bj.scb.service;

import java.util.Map;
import com.bj.scb.pojo.Role;
import com.bj.scb.utils.PageList;

public interface RoleService {
	
	PageList<Role> queryRolePageList(Map<String, Object> parameter);

	Role getRoleById(String roleId);

	void saveRole(Role role);

	void deleteRole(String roleId);
}
