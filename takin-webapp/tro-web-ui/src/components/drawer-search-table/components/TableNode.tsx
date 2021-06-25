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
import React, { useContext } from 'react';
import { Row, Col, Card, Tabs, Icon, Spin } from 'antd';
import { CommonTable } from 'racc';
import { SearchTableProps } from '../type';
import { SearchTableContext } from '../context';
import styles from '../index.less';

const TableNode: React.FC<SearchTableProps> = props => {
  const { state, setState } = useContext(SearchTableContext);
  const renderTitle = (): React.ReactNode => {
    if (!props.tabsData) {
      return null;
    }
    const renderTabsData = [...props.tabsData];
    renderTabsData.unshift({
      label: '全部',
      value: ''
    });
    return (
      <Tabs
        style={{ width: '100%', overflow: 'hidden', padding: '0 16px' }}
        activeKey={state.searchParams[props.tabKey] || ''}
        type="line"
        onChange={tabKey => {
          setState({
            searchParams: {
              ...state.searchParams,
              [props.tabKey]: tabKey,
              current: 0
            },
            checkedKeys: []
          });
        }}
      >
        {renderTabsData.map((item, index) => (
          <Tabs.TabPane tab={item.label} key={item.value.toString()} />
        ))}
      </Tabs>
    );
  };
  const handleReload = () => {
    // state.form.resetFields();
    setState({
      toggleRoload: !state.toggleRoload
    });
  };
  const handleCheck = (checkedKeys, checkedRows) => {
    setState({ checkedKeys, checkedRows });
    if (props.onCheck) {
      props.onCheck(checkedKeys, checkedRows);
    }
  };
  return (
    <Card
      headStyle={{ padding: 0, borderBottom: 'none', fontSize: 14 }}
      bodyStyle={{ padding: '0 16px 16px' }}
      title={renderTitle()}
      className={styles.tableWrap}
    >
      <Row
        type="flex"
        justify="space-between"
        align="middle"
        className={!props.tabsData ? 'mg-t1x mg-b1x' : 'mg-b1x'}
      >
        <Col>
          <Icon
            onClick={() => handleReload()}
            style={{ color: '#C5C7DC' }}
            className={styles.reloadIcon}
            type="reload"
          />
        </Col>
        <Col>{props.tableAction}</Col>
      </Row>
      {props.renderTable ? (
        <Spin spinning={state.loading}>
          {props.renderTable(state.dataSource)}
        </Spin>
      ) : (
        <CommonTable
          rowKey={(row, index) => index.toString()}
          size="small"
          {...props.commonTableProps}
          dataSource={
            props.dataKey ? state.dataSource[props.dataKey] : state.dataSource
          }
          rowSelectProps={{
            ...props.commonTableProps.rowSelectProps,
            selectedRowKeys: state.checkedKeys
          }}
          onCheck={handleCheck}
          loading={state.loading}
        />
      )}
    </Card>
  );
};
export default TableNode;
