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

import { Table, Transfer } from 'antd';
import { TableRowSelection } from 'antd/lib/table';
import { difference } from 'lodash';
import { CommonModal, useStateReducer } from 'racc';
import React, { Fragment } from 'react';
interface Props {
  btnText?: string | React.ReactDOM;
}
const getInitState = () => ({
  dataSource: [],
  selectKeys: [],
  loading: false,
  /**
   * @name 初始被选中的应用，array
   */
  targetKeys: ['1', '2']
});
export type WhiteListScopeModalState = ReturnType<typeof getInitState>;
const WhiteListScopeModal: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<WhiteListScopeModalState>(
    getInitState()
  );
  const TableTransfer = ({ leftColumns, rightColumns, ...restProps }) => (
    <Transfer {...restProps}>
      {({
        direction,
        filteredItems,
        onItemSelectAll,
        onItemSelect,
        selectedKeys: listSelectedKeys,
        disabled: listDisabled
      }) => {
        const columns = direction === 'left' ? leftColumns : rightColumns;
        const rowSelection: TableRowSelection<any> = {
          getCheckboxProps: item => ({
            disabled: listDisabled || item.disabled
          }),
          //   onSelectAll(selected) {onItemSelectAll
          //     const treeSelectedKeys = filteredItems
          //       .filter(item => !item.disabled)
          //       .map(({ key }) => key);
          //     const selectKeys = selected ? treeSelectedKeys : [];
          //     onItemSelectAll(selected ? selectKeys : treeSelectedKeys, selected);
          //     setState({ selectKeys });
          //   },
          //   onSelect({ key }, selected) {
          //     onItemSelect(key, selected);
          //     console.log(key, selected);
          //     const selectKeys = selected
          //       ? [...state.selectKeys, key]
          //       : difference(state.selectKeys, [key]);
          //     setState({
          //       selectKeys
          //     });
          //   },
          onSelect({ key }, selected) {
            onItemSelect(key, selected);
            // console.log('key, selected', key, selected);
            const selectKeys = selected
              ? [...state.selectKeys, key]
              : difference(state.selectKeys, [key]);
            setState({
              selectKeys
            });
          },
          onSelectAll(selected, selectedRows) {
            // console.log('selected, selectedRows', selected, selectedRows);
            const treeSelectedKeys = selectedRows
              .filter(item => !item.disabled)
              .map(({ key }) => key);
            // console.log('treeSelectedKeys', treeSelectedKeys);
            // console.log('listSelectedKeys', listSelectedKeys);
            const selectKeys = selected ? treeSelectedKeys : [];
            // const diffKeys = selected
            //   ? difference(treeSelectedKeys, listSelectedKeys)
            //   : difference(listSelectedKeys, treeSelectedKeys);
            // console.log(
            //   'difference(treeSelectedKeys, listSelectedKeys)',
            //   difference(treeSelectedKeys, listSelectedKeys)
            // );
            // console.log(selected ? selectKeys : treeSelectedKeys, selected);
            setState({ selectKeys });
          },

          selectedRowKeys: state.selectKeys
        };

        return (
          <Table
            rowSelection={rowSelection}
            columns={columns}
            dataSource={filteredItems}
            size="small"
            style={{ pointerEvents: listDisabled ? 'none' : null }}
            loading={state.loading}
            // onRow={({ key, disabled: itemDisabled }) => ({
            //   onClick: () => {
            //     if (itemDisabled || listDisabled) return;
            //     onItemSelect(key, !listSelectedKeys.includes(key));
            //   }
            // })}
          />
        );
      }}
    </Transfer>
  );

  const onChange = nextTargetKeys => {
    setState({
      targetKeys: nextTargetKeys,
      selectKeys: []
    });
  };
  const leftTableColumns = [
    {
      dataIndex: 'app',
      title: '应用名称'
    }
  ];
  const rightTableColumns = [
    {
      dataIndex: 'app',
      title: '应用名称'
    }
  ];
  return (
    <CommonModal
      modalProps={{
        width: 1096,
        title: '白名单生效范围'
      }}
      btnProps={{ type: 'link' }}
      btnText={props.btnText}
    >
      <TableTransfer
        dataSource={state.dataSource}
        targetKeys={state.targetKeys}
        onChange={onChange}
        leftColumns={leftTableColumns}
        rightColumns={rightTableColumns}
        showSelectAll={false}
        listStyle={{ maxHeight: 600, overflow: 'auto' }}
        // filterOption={(inputValue, item) => {
        //   console.log('inputValue, item', inputValue, item);
        // }}
        // showSearch={true}
      />
    </CommonModal>
  );
};
export default WhiteListScopeModal;
