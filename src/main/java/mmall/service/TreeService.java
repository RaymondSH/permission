package mmall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import mmall.dao.ACLMapper;
import mmall.dao.ACLModuleMapper;
import mmall.dao.DepartmentMapper;
import mmall.dto.ACLDTO;
import mmall.dto.ACLModuleDTO;
import mmall.dto.DepartmentLevelDTO;
import mmall.model.ACL;
import mmall.model.ACLModule;
import mmall.model.Department;
import mmall.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Raymond on 2019/1/10.
 */
@Service
public class TreeService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ACLModuleMapper moduleMapper;

    @Autowired
    private CoreService coreService;

    @Autowired
    private ACLMapper aclMapper;


    public List<ACLModuleDTO> userAclTree(int userId){
        List<ACL> userAclList = coreService.getUserACLById(userId);
        List<ACLDTO> aclDTOList = Lists.newArrayList();
        for (ACL acl : userAclList){
            ACLDTO dto = ACLDTO.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDTOList.add(dto);
        }
        return ACLToTree(aclDTOList);
    }


    public List roleTree(int roleId){
        //1、当前用户分配的权限点
        List<ACL> userAclList = coreService.getCurrentUserACL();
        //2、当前角色分配的权限点
        List<ACL> roleAclList = coreService.getRoleACL(roleId);

        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        List<ACL> allAcl = aclMapper.getAll();

        List<ACLDTO> aclDTOList = Lists.newArrayList();
        for (ACL acl : allAcl){
            ACLDTO dto = ACLDTO.adapt(acl);
            if (userAclIdSet.contains(acl.getId())){
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl)){
                dto.setChecked(true);
            }
            aclDTOList.add(dto);
        }
        return ACLToTree(aclDTOList);
    }

    private List<ACLModuleDTO> ACLToTree(List<ACLDTO> aclDtoList){
        if (CollectionUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        List<ACLModuleDTO> moduleDTOList = moduleTree();

        Multimap<Integer,ACLDTO> moduleIdAclMap = ArrayListMultimap.create();
        for (ACLDTO acl : aclDtoList){
            if (acl.getStatus() == 1){
                moduleIdAclMap.put(acl.getAclModuleId(),acl);
            }
        }
        bindAclsWithOrder(moduleDTOList,moduleIdAclMap);
        return moduleDTOList;
    }

    private void bindAclsWithOrder(List<ACLModuleDTO> moduleDTOList,Multimap<Integer,ACLDTO> moduleIdAclMap){
        if (CollectionUtils.isEmpty(moduleDTOList)){
            return;
        }
        for (ACLModuleDTO dto : moduleDTOList){
            List<ACLDTO> aclDtoList = (List<ACLDTO>)moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)){
                Collections.sort(aclDtoList,aclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(),moduleIdAclMap);
        }
    }


    public List<ACLModuleDTO> moduleTree(){
        List<ACLModule> moduleList = moduleMapper.getAllModule();
        List<ACLModuleDTO> moduleDTOList = Lists.newArrayList();
        for (ACLModule module : moduleList){
            moduleDTOList.add(ACLModuleDTO.adapt(module));
        }
        return moduleListToTree(moduleDTOList);
    }
    public List<ACLModuleDTO> moduleListToTree(List<ACLModuleDTO> moduleDTOList){
        if (CollectionUtils.isEmpty(moduleDTOList)){
            return Lists.newArrayList();
        }
        Multimap<String,ACLModuleDTO> levelMap = ArrayListMultimap.create();
        List<ACLModuleDTO> rootList = Lists.newArrayList();

        for (ACLModuleDTO levelDTO : moduleDTOList) {
            levelMap.put(levelDTO.getLevel(),levelDTO);
            if (LevelUtil.ROOT.equals(levelDTO.getLevel())){
                rootList.add(levelDTO);
            }
        }
        Collections.sort(rootList, moduleSeqComparator);
        transformModuleTree(rootList,LevelUtil.ROOT,levelMap);
        return rootList;
    }

    public void transformModuleTree(List<ACLModuleDTO> moduleDTOList, String level, Multimap<String,ACLModuleDTO> levelMap){
        for (int i = 0; i < moduleDTOList.size(); i++) {
            //遍历该层每一个元素
            ACLModuleDTO moduleDTO = moduleDTOList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level,moduleDTO.getId());
            //处理下一层
            List<ACLModuleDTO> tempModuleList = (List<ACLModuleDTO>)levelMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempModuleList)){
                //排序
                Collections.sort(tempModuleList,moduleSeqComparator);
                //进入下一层部门
                moduleDTO.setAclModuleList(tempModuleList);
                //到下一层去处理
                transformModuleTree(tempModuleList,nextLevel,levelMap);
            }
        }
    }

    public Comparator<ACLModuleDTO> moduleSeqComparator = new Comparator<ACLModuleDTO>() {
        @Override
        public int compare(ACLModuleDTO o1, ACLModuleDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public List<DepartmentLevelDTO> departmentTree(){
        List<Department> departmentList = departmentMapper.getAllDepartment();
        List<DepartmentLevelDTO> departmentLevelDTOList = Lists.newArrayList();
        for (Department department : departmentList){
            DepartmentLevelDTO departmentLevelDTO = DepartmentLevelDTO.adapt(department);
            departmentLevelDTOList.add(departmentLevelDTO);
        }
        return departmentListToTree(departmentLevelDTOList);
    }

    public List<DepartmentLevelDTO> departmentListToTree(List<DepartmentLevelDTO> levelDTOList){
        if (CollectionUtils.isEmpty(levelDTOList)){
            return Lists.newArrayList();
        }
        Multimap<String,DepartmentLevelDTO> levelMap = ArrayListMultimap.create();
        List<DepartmentLevelDTO> rootList = Lists.newArrayList();

        for (DepartmentLevelDTO levelDTO : levelDTOList) {
            levelMap.put(levelDTO.getLevel(),levelDTO);
            if (LevelUtil.ROOT.equals(levelDTO.getLevel())){
                rootList.add(levelDTO);
            }
        }

        Collections.sort(rootList, new Comparator<DepartmentLevelDTO>() {
            @Override
            public int compare(DepartmentLevelDTO o1, DepartmentLevelDTO o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        transformDepartmentTree(levelDTOList,LevelUtil.ROOT,levelMap);
        return rootList;
    }

    public void transformDepartmentTree(List<DepartmentLevelDTO> departmentLevelDTOList,String level,Multimap<String,DepartmentLevelDTO> levelMap){
        for (int i = 0; i < departmentLevelDTOList.size(); i++) {
            //遍历该层每一个元素
            DepartmentLevelDTO departmentLevelDTO = departmentLevelDTOList.get(i);
            //处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level,departmentLevelDTO.getId());
            //处理下一层
            List<DepartmentLevelDTO> tempDepartmentList = (List<DepartmentLevelDTO>)levelMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDepartmentList)){
                //排序
                Collections.sort(tempDepartmentList,departmentSeqComparator);
                //进入下一层部门
                departmentLevelDTO.setDeptList(tempDepartmentList);
                //到下一层去处理
                transformDepartmentTree(tempDepartmentList,nextLevel,levelMap);
            }
        }
    }

    public Comparator<DepartmentLevelDTO> departmentSeqComparator = new Comparator<DepartmentLevelDTO>() {
        @Override
        public int compare(DepartmentLevelDTO o1, DepartmentLevelDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    public Comparator<ACLDTO> aclSeqComparator = new Comparator<ACLDTO>() {
        @Override
        public int compare(ACLDTO o1, ACLDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

}
