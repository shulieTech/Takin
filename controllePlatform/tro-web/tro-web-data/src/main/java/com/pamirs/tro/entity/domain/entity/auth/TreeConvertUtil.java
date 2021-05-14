/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pamirs.tro.entity.domain.entity.auth;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.shulie.tro.web.data.result.user.ResourceMenuResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: fanxx
 * @Date: 2020/9/3 下午3:33
 * @Description:
 */
public class TreeConvertUtil {


    public static List<TreeDeptModel> convertToTree(List<Dept> data) {
        //设置一级部门ParentId为0，根据ParentId分组
        Map<Long, List<Dept>> deptMap = data.stream().map(item -> {
            if (Objects.isNull(item.getParentId())) {
                item.setParentId(0L);
            }
            return item;
        }).collect(Collectors.groupingBy(Dept::getParentId));

        List<TreeDeptModel> result = Lists.newArrayList();
        //一级部门为空，返回空
        if (!deptMap.containsKey(0L)) {
            return result;
        }
        //一级部门为根节点，遍历叶子节点
        for (Dept dept : deptMap.get(0L)) {
            TreeDeptModel treeDeptModel = getTreeDeptModel(dept);
            treeDeptModel.setChildren(getChildren(dept.getId(), deptMap));
            result.add(treeDeptModel);
        }

        return sortChildren(result);
    }

    private static List<TreeDeptModel> sortChildren(List<TreeDeptModel> deptList) {
        if (CollectionUtils.isEmpty(deptList)) {
            return deptList;
        }
        for (TreeDeptModel deptModel : deptList) {
            if (CollectionUtils.isNotEmpty(deptModel.getChildren())) {
                deptModel.setChildren(sortChildren(deptModel.getChildren()));
            }
        }

        return deptList.stream().sorted(Comparator.comparing(TreeDeptModel::getId)).collect(Collectors.toList());
    }

    private static List<TreeDeptModel> getChildren(Long parentId, Map<Long, List<Dept>> deptMap) {
        if (!deptMap.containsKey(parentId)) {
            return null;
        }
        List<TreeDeptModel> result = Lists.newArrayList();
        for (Dept dept : deptMap.get(parentId)) {
            TreeDeptModel treeDeptModel = getTreeDeptModel(dept);
            treeDeptModel.setChildren(getChildren(dept.getId(), deptMap));
            result.add(treeDeptModel);
        }
        return result;

    }

    private static TreeDeptModel getTreeDeptModel(Dept dept) {
        TreeDeptModel treeDeptModel = new TreeDeptModel();
        treeDeptModel.setKey(String.valueOf(dept.getSequence()));
        treeDeptModel.setTitle(dept.getName());
        treeDeptModel.setOrder(dept.getSequence());
        treeDeptModel.setParentId(dept.getParentId());
        treeDeptModel.setId(dept.getId());
        treeDeptModel.setCheckEnable(true);
        return treeDeptModel;
    }

    public static void convertToTree(List<Dept> data, List<Dept> parent) {
        List<Dept> nextLevel = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(parent)) {
            parent.stream().forEach(item -> {
                data.stream().forEach(tmp -> {
                    if (Objects.equals(item.getId(), tmp.getParentId())) {
                        List<Dept> nodes = item.getChildrenNodes();
                        if (Objects.isNull(nodes)) {
                            nodes = Lists.newArrayList();
                            item.setChildrenNodes(nodes);
                        }
                        nodes.add(tmp);
                        nextLevel.add(tmp);
                    }
                });
            });
        } else {
            data
                    .stream()
                    .forEach(item -> {
                        if (Objects.equals(0L, item.getParentId())) {
                            item.setParentId(null);
                        }
                    });
            List<Dept> tmp = data
                    .stream()
                    .filter(item -> Objects.isNull(item.getParentId()))
                    .collect(Collectors.toList());
            nextLevel.addAll(tmp);
        }
        if (CollectionUtils.isNotEmpty(nextLevel)) {
            convertToTree(data, nextLevel);
        }
    }

    public static TreeDeptModel convertTreeDeptModel(Dept dept) {
        if (Objects.isNull(dept)) {
            return null;
        }
        TreeDeptModel deptModel = new TreeDeptModel();
        deptModel.setId(dept.getId());
        deptModel.setKey(String.valueOf(dept.getSequence()));
        deptModel.setTitle(dept.getName());
        deptModel.setOrder(dept.getSequence());
        deptModel.setParentId(dept.getParentId());
        deptModel.setCheckEnable(true);
        List<Dept> nodes = dept.getChildrenNodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            List<TreeDeptModel> childs = Lists.newArrayList();
            for (Dept node : nodes) {
                childs.add(convertTreeDeptModel(node));
            }
            deptModel.setChildren(childs);
        }
        return deptModel;
    }

    public static List<String> getEndDeptByDeptIds(List<Dept> deptTotalList, List<String> deptIds,
                                                   List<String> endDeptIdList) {
        List<String> nextLevel = Lists.newArrayList();
        List<String> parentIds = deptTotalList.stream().map(Dept::getParentId).map(String::valueOf).collect(
                Collectors.toList());
        if (CollectionUtils.isNotEmpty(deptIds)) {
            deptIds.stream().forEach(currentId -> {
                if (parentIds.contains(currentId)) {
                    deptTotalList.stream().forEach(tmp -> {
                        if (StringUtils.equals(currentId, String.valueOf(tmp.getParentId()))) {
                            nextLevel.add(String.valueOf(tmp.getId()));
                        }
                    });
                } else {
                    endDeptIdList.add(currentId);
                }
            });
        } else {
            deptTotalList
                    .stream()
                    .forEach(item -> {
                        if (Objects.equals(0L, item.getParentId())) {
                            item.setParentId(null);
                        }
                    });
            List<String> tmp = deptTotalList
                    .stream()
                    .filter(item -> Objects.isNull(item.getParentId())).map(Dept::getId).map(String::valueOf).collect(
                            Collectors.toList());
            nextLevel.addAll(tmp);
        }

        if (CollectionUtils.isNotEmpty(nextLevel)) {
            getEndDeptByDeptIds(deptTotalList, nextLevel, endDeptIdList);
        } else {
            if (CollectionUtils.isNotEmpty(deptIds)) {
                endDeptIdList.addAll(deptIds);
            }
        }
        return endDeptIdList;
    }

    public static Set<String> getLowerDeptByParentDeptIds(List<Dept> deptTotalList, Set<String> deptIds,
                                                          Set<String> lowerDeptIdList) {
        Set<String> nextLevel = Sets.newHashSet();
        List<String> parentIds = deptTotalList.stream().filter(dept -> dept.getParentId() != null).map(Dept::getParentId).distinct().map(String::valueOf).collect(
                Collectors.toList());
        if (CollectionUtils.isNotEmpty(deptIds)) {
            deptIds.stream().forEach(currentId -> {
                if (parentIds.contains(currentId)) {
                    deptTotalList.stream().forEach(tmp -> {
                        if (StringUtils.equals(currentId, String.valueOf(tmp.getParentId()))) {
                            nextLevel.add(String.valueOf(tmp.getId()));
                        }
                    });
                }
            });
        }
        if (CollectionUtils.isNotEmpty(nextLevel)) {
            lowerDeptIdList.addAll(nextLevel);
            getLowerDeptByParentDeptIds(deptTotalList, nextLevel, lowerDeptIdList);
        } else {
            if (CollectionUtils.isNotEmpty(deptIds)) {
                lowerDeptIdList.addAll(deptIds);
            }
        }
        return lowerDeptIdList;
    }

    public static List<ResourceMenuResult> getParentResourceByResourceIdList(List<ResourceMenuResult> menuTotalList
            , List<ResourceMenuResult> endResourceList) {
        List<ResourceMenuResult> forwardResourceList = Lists.newArrayList();
        endResourceList.forEach(endResource -> {
            if (!Objects.isNull(endResource.getParentId())) {
                Optional<ResourceMenuResult> optional = menuTotalList.
                        stream()
                        .filter(resourceMenuResult -> resourceMenuResult.getId().equals(endResource.getParentId()))
                        .findFirst();
                optional.ifPresent(forwardResourceList::add);
            }
        });
        endResourceList.addAll(forwardResourceList);
        if (CollectionUtils.isNotEmpty(forwardResourceList)) {
            getParentResourceByResourceIdList(menuTotalList, forwardResourceList);
        }
        return forwardResourceList;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
