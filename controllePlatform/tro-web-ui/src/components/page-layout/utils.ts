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

import { MenuBean } from 'src/common/menu/type';

/**
 * @name 获取面包屑
 * @param { menuData: 菜单数据源, pathname: 当前路由, breadCrumbs: 面包屑 }
 */
export const filterBreadCrumbs = (
  menuData: MenuBean[],
  pathname: string,
  breadCrumbs: any[]
): MenuBean => {
  return menuData.find(item => {
    breadCrumbs.push(item);
    if (item.path === pathname) {
      return true;
    }
    if (item.children) {
      const isFind = filterBreadCrumbs(item.children, pathname, breadCrumbs);
      if (isFind) {
        return true;
      }
    }
    breadCrumbs.pop();
    return false;
  });
};
