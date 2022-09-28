package com.mycompany.jwtdemo.service;

import com.mycompany.jwtdemo.model.RoleModel;
import java.util.List;

public interface RoleService {
    public RoleModel createRole(RoleModel roleModel);
    public List<RoleModel> getAllRoles();
    public RoleModel getRoleById(Long id);
    public void deleteRoleById(Long id);

}
