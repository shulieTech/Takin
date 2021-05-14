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

/**
 * @name
 * @author MingShined
 */
import React from 'react';
import { MenuBean, MenuType } from 'src/common/menu/type';

import { Menu, Icon } from 'antd';
import Link from 'umi/link';
import menuList from 'src/common/menu';

const SubMenu = Menu.SubMenu;

const renderMenuNode = (): React.ReactNode => {
  const renderMenuItem = (menusData: MenuBean[]): React.ReactNode => {
    if (!menusData) {
      return [];
    }

    function renderTitle(item: MenuBean) {
      return item.icon ? (
        <span>
          {/* {getIcon(item.icon)} */}
          <span>{item.title}</span>
        </span>
      ) : (
        item.title
      );
    }

    // const newMenusData = menusData.filter(item => {
    //   if (item.key.includes(localStorage.getItem('troweb-role'))) {
    //     return item;
    //   }
    // });

    /**
     * @name 过滤有权限展示的menu
     */
    const userAuthority = localStorage.getItem('trowebUserResource');

    const newMenusData = menusData.filter(
      item => userAuthority && userAuthority.indexOf(item.key) !== -1
    );
    // return menusData.map((item: MenuBean) => {
    return newMenusData.map((item: MenuBean) => {
      let view = null;
      switch (item.type) {
        case MenuType.SubMenu:
          view = (
            <SubMenu title={<MenuTitle menuItem={item} />} key={item.path}>
              {renderMenuItem(item.children)}
            </SubMenu>
          );
          break;
        case MenuType.ItemGroup:
          view = (
            <Menu.ItemGroup
              title={<MenuTitle menuItem={item} />}
              key={item.path}
            >
              {renderMenuItem(item.children)}
            </Menu.ItemGroup>
          );
          break;
        case MenuType.Item:
          view = (
            <Menu.Item key={item.path}>
              <Link to={item.path}>{<MenuTitle menuItem={item} />}</Link>
            </Menu.Item>
          );
          break;
        case MenuType.Url:
          view = (
            <Menu.Item key={item.path}>
              <a href={item.path} target="_blank">
                {<MenuTitle menuItem={item} />}
              </a>
            </Menu.Item>
          );
          break;
        default:
          view = null;
          break;
      }
      return view;
    });
  };
  // const basicMenuData = [
  //   {
  //     title: '链路标记',
  //     path: '/linkMark',
  //     type: MenuType.Item,
  //     icon: 'home'
  //   },
  //   {
  //     title: '系统流程',
  //     path: '/systemFlow',
  //     type: MenuType.Item,
  //     icon: 'line-chart'
  //   },
  //   {
  //     title: '业务活动',
  //     path: '/businessActivity',
  //     type: MenuType.Item,
  //     icon: 'control'
  //   },
  //   {
  //     title: '业务流程',
  //     path: '/businessFlow',
  //     type: MenuType.Item,
  //     icon: 'dot-chart',
  //     children: [
  //       {
  //         title: '新增业务流程',
  //         path: '/businessFlow/addBusinessFlow',
  //         type: MenuType.NoMenu
  //       }
  //     ]
  //   }
  // ];

  // const adminMenusData = [
  //   {
  //     title: '应用管理',
  //     path: '/appManage',
  //     type: MenuType.Item,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '应用详情',
  //         path: '/appManage/details',
  //         type: MenuType.NoMenu
  //       }
  //     ]
  //   },
  //   {
  //     title: '客户管理',
  //     path: '/userManage',
  //     type: MenuType.Item,
  //     icon: 'user'
  //   },
  //   {
  //     title: '应用管理',
  //     path: '/appTrialManage',
  //     type: MenuType.Item,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '应用详情',
  //         path: '/appTrialManage/details',
  //         type: MenuType.NoMenu
  //       }
  //     ]
  //   },
  //   {
  //     title: '配置中心',
  //     path: '/configCenter',
  //     type: MenuType.SubMenu,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '压测开关设置',
  //         path: '/configCenter/pressureMeasureSwitch',
  //         type: MenuType.Item
  //       }
  //     ]
  //   },
  //   {
  //     title: '压测管理',
  //     path: '/pressureTestManage',
  //     type: MenuType.SubMenu,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '压测场景',
  //         path: '/pressureTestManage/pressureTestScene',
  //         type: MenuType.Item,
  //         children: [
  //           {
  //             title: '压测场景配置',
  //             path:
  //               '/pressureTestManage/pressureTestScene/pressureTestSceneConfig',
  //             type: MenuType.NoMenu
  //           }
  //         ]
  //       },
  //       {
  //         title: '压测报告',
  //         path: '/pressureTestManage/pressureTestReport',
  //         type: MenuType.Item,
  //         children: [
  //           {
  //             title: '压测实况',
  //             path: '/pressureTestManage/pressureTestReport/pressureTestLive',
  //             type: MenuType.NoMenu
  //           },
  //           {
  //             title: '压测报告详请',
  //             path: '/pressureTestManage/pressureTestReport/details',
  //             type: MenuType.NoMenu
  //           }
  //         ]
  //       }
  //     ]
  //   }
  // ];

  // const trialUserMenusData = [
  //   {
  //     title: '应用管理',
  //     path: '/appTrialManage',
  //     type: MenuType.Item,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '应用详情',
  //         path: '/appTrialManage/details',
  //         type: MenuType.NoMenu
  //       }
  //     ]
  //   }
  // ];

  // const formalUserMenusData = [
  //   {
  //     title: '应用管理',
  //     path: '/appManage',
  //     type: MenuType.Item,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '应用详情',
  //         path: '/appManage/details',
  //         type: MenuType.NoMenu
  //       }
  //     ]
  //   },
  //   {
  //     title: '配置中心',
  //     path: '/configCenter',
  //     type: MenuType.SubMenu,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '压测开关设置',
  //         path: '/configCenter/pressureMeasureSwitch',
  //         type: MenuType.Item
  //       }
  //     ]
  //   },
  //   {
  //     title: '压测管理',
  //     path: '/pressureTestManage',
  //     type: MenuType.SubMenu,
  //     icon: 'setting',
  //     children: [
  //       {
  //         title: '压测场景',
  //         path: '/pressureTestManage/pressureTestScene',
  //         type: MenuType.Item,
  //         children: [
  //           {
  //             title: '压测场景配置',
  //             path:
  //               '/pressureTestManage/pressureTestScene/pressureTestSceneConfig',
  //             type: MenuType.NoMenu
  //           }
  //         ]
  //       },
  //       {
  //         title: '压测报告',
  //         path: '/pressureTestManage/pressureTestReport',
  //         type: MenuType.Item,
  //         children: [
  //           {
  //             title: '压测实况',
  //             path: '/pressureTestManage/pressureTestReport/pressureTestLive',
  //             type: MenuType.NoMenu
  //           },
  //           {
  //             title: '压测报告详请',
  //             path: '/pressureTestManage/pressureTestReport/details',
  //             type: MenuType.NoMenu
  //           }
  //         ]
  //       }
  //     ]
  //   }
  // ];

  // let newMenusData;
  // if (localStorage.getItem('troweb-role') === '0') {
  //   newMenusData = basicMenuData.concat(adminMenusData);
  // } else if (localStorage.getItem('troweb-role') === '1') {
  //   newMenusData = trialUserMenusData;
  // } else if (localStorage.getItem('troweb-role') === '2') {
  //   newMenusData = basicMenuData.concat(formalUserMenusData);
  // } else {
  //   newMenusData = null;
  // }
  // return renderMenuItem(newMenusData);
  return renderMenuItem(menuList);
};
export default renderMenuNode;

const IconNode: React.FC<{ icon: string }> = ({ icon }) => {
  if (typeof icon === 'string' && icon.indexOf('http') === 0) {
    return <img src={icon} alt="icon" />;
  }
  if (typeof icon === 'string') {
    return <Icon type={icon} />;
  }
  return icon;
};

const MenuTitle: React.FC<any> = ({ menuItem }) => {
  return menuItem.icon ? (
    <span>
      <IconNode icon={menuItem.icon} />
      <span>{menuItem.title}</span>
    </span>
  ) : (
    menuItem.title
  );
};
