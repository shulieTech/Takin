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
import { Col, Pagination, Row } from 'antd';
import React, { useContext } from 'react';
import { SearchTableContext } from '../context';
import styles from '../index.less';
import { SearchTableProps } from '../type';

const FooterNode: React.FC<SearchTableProps> = props => {
  const { state, setState } = useContext(SearchTableContext);
  const handleChange = (page, size) => {
    setState({
      searchParams: {
        ...state.searchParams,
        current: page - 1,
        pageSize: size
      },
      checkedKeys: []
    });
  };
  return (
    <div className={styles.footer}>
      <Row align="middle" type="flex" justify="space-between">
        <Col>{props.footerAction ? props.footerAction : <span />}</Col>
        <Col>
          <Pagination
            current={state.searchParams.current + 1}
            total={state.total}
            showTotal={(t, range) =>
              `共 ${state.total} 条数据 第${state.searchParams.current +
                1}页 / 共 ${Math.ceil(
                state.total / (state.searchParams.pageSize || 10)
              )}页`
            }
            pageSize={state.searchParams.pageSize}
            showQuickJumper
            showSizeChanger
            onChange={handleChange}
            onShowSizeChange={handleChange}
          />
        </Col>
      </Row>
    </div>
  );
};
export default FooterNode;
