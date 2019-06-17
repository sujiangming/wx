package com.bj.scb.service.impl;

import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.RoleDaoImpl;
import com.bj.scb.pojo.Role;
import com.bj.scb.service.RoleService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Resource
	RoleDaoImpl roleDao;

	@Override
	public PageList<Role> queryRolePageList(Map<String, Object> parameter) {
		return roleDao.queryRolePageList(parameter);
	}

	@Override
	public Role getRoleById(String roleId) {
		return roleDao.getRoleById(roleId);
	}

	@Override
	public void saveRole(Role role) {
		roleDao.saveRole(role);
	}

	@Override
	public void deleteRole(String roleId) {
		roleDao.deleteRole(roleId);
	}

}
