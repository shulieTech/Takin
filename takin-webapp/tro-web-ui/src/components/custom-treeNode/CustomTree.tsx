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
 * @author chuxu
 */

import React, { Fragment } from 'react';
import { Tree } from 'antd';
import styles from './index.less';
import { TreeProps } from 'antd/lib/tree';
interface Props {
  dataSource: any;
  /**
   * @name 作为title展示的数据结构中的字段key名
   */
  key: string;
  titleKey: string;
  treeProps?: TreeProps;
}
const CustomTree: React.FC<Props> = props => {
  const { dataSource, key, titleKey } = props;

  /**
   * @name 定制的展示树节点（要求有三段）
   */
  const titleNode = titleItem => {
    return (
      titleItem && (
        <p>
          <span className={styles.firstColor}>{titleItem.split('|')[0]}</span>
          <span className={styles.thirdColor}>|</span>
          <span className={styles.secondColor}>{titleItem.split('|')[1]}</span>
          <span className={styles.thirdColor}>|</span>
          <span className={styles.thirdColor}>{titleItem.split('|')[2]}</span>
        </p>
      )
    );
  };

  const renderTreeNodes = data => {
    return (
      data &&
      data.map(item => {
        if (item.children && item.children.length) {
          return (
            <Tree.TreeNode
              title={titleNode(item[titleKey])}
              key={item.id}
              dataRef={item}
              treeDefaultExpandAll={true}
              style={{ color: '#fff', width: 100 }}
            >
              {renderTreeNodes(item.children)}
            </Tree.TreeNode>
          );
        }
        return (
          <Tree.TreeNode
            style={{ color: '#fff' }}
            key={item.id}
            dataRef={item}
            title={titleNode(item[titleKey])}
            children={item.children}
          />
        );
      })
    );
  };
  return (
    <Tree defaultExpandAll style={{ color: '#fff' }} {...props.treeProps}>
      {renderTreeNodes(dataSource)}
    </Tree>
  );
};
export default CustomTree;
