package com.bj.scb.service;

import java.util.Map;

import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Role;
import com.bj.scb.utils.PageList;

public interface RoleMemberService {
	
	PageList<InnerUser> queryRolePageList(Map<String, Object> parameter);

	Role getRoleById(String roleId);

	void saveRole(InnerUser role);

	void deleteRole(String roleId);

	int updateRoleMember(InnerUser oldAdmin);
}
