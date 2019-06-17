package com.bj.scb.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.RoleMemberDaoImpl;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Role;
import com.bj.scb.service.RoleMemberService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class RoleMemberServiceImpl implements RoleMemberService {

	@Resource(name="roleMemberDaoImpl")
	RoleMemberDaoImpl roleMemberDaoImpl;

	@Override
	public PageList<InnerUser> queryRolePageList(Map<String, Object> parameter) {
		return roleMemberDaoImpl.queryRolePageList(parameter);
	}

	@Override
	public Role getRoleById(String roleId) {
		return roleMemberDaoImpl.getRoleById(roleId);
	}

	@Override
	public void saveRole(InnerUser role) {
		roleMemberDaoImpl.saveRole(role);
	}

	@Override
	public void deleteRole(String roleId) {
		roleMemberDaoImpl.deleteRole(roleId);
	}

	@Override
	public int updateRoleMember(InnerUser oldAdmin) {
		return roleMemberDaoImpl.updateRoleMember(oldAdmin);
	}

}
