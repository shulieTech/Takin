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
import React, { useContext, useEffect } from 'react';
import { CommonForm, separateArrayToKey } from 'racc';
import { SearchTableProps } from '../type';
import { SearchTableContext } from '../context';
import { Row, Col, Icon } from 'antd';
import ToolTipIcon from 'src/common/tooltip-icon';

const SearchNode: React.FC<SearchTableProps> = props => {
  const { state, setState } = useContext(SearchTableContext);

  const handleSearch = (err, values) => {
    if (state.loading) {
      return;
    }
    let searchParams = {
      ...state.searchParams,
      ...values,
      current: 0
    };
    if (props.onTabSearch && !props.onTabSearch(searchParams)) {
      return;
    }
    if (props.datekeys) {
      searchParams = separateArrayToKey(searchParams, props.datekeys);
    }
    if (props.cascaderKeys) {
      searchParams = separateArrayToKey(searchParams, props.cascaderKeys);
    }
    setState({ searchParams });
  };
  const handleReset = () => {
    if (state.loading) {
      return;
    }
    if (!props.commonFormProps || !state.form) {
      return;
    }
    const resetParams = {};
    props.commonFormProps.formData.forEach(item => {
      resetParams[item.key] = undefined;
    });
    if (props.datekeys) {
      props.datekeys.forEach(item => {
        item.separateKey.forEach(key => {
          resetParams[key] = undefined;
        });
      });
    }
    if (props.cascaderKeys) {
      props.cascaderKeys.forEach(item => {
        item.separateKey.forEach(key => {
          resetParams[key] = undefined;
        });
      });
    }
    const searchParams = { ...state.searchParams, ...resetParams };
    state.form.resetFields();
    if (props.onTabReset) {
      props.onTabReset(searchParams);
      // return;
    }
    setState({ searchParams });
  };
  return (
    <Row
      type="flex"
      style={{ transform: `translateY(${props.filterData ? '13' : '8'}px)` }}
    >
      {/* <Col style={{ width: 50, lineHeight: '40px' }} className="ft-white">
        搜索：
      </Col> */}
      <Col className="flex-1" style={{ minHeight: 40 }}>
        <CommonForm
          mode="shrink"
          btnProps={{
            place: 'start',
            submitText: '查询',
            resetBtnProps: {
              style: { marginLeft: -8 }
            },
            shrinkNode: (
              <ToolTipIcon
                iconName="shrink_icon"
                imgStyle={{ ...shrinkIconStyle, transform: 'rotate(-180deg)' }}
              />
            ),
            expandNode: (
              <ToolTipIcon iconName="shrink_icon" imgStyle={shrinkIconStyle} />
            )
          }}
          {...props.commonFormProps}
          onSubmit={handleSearch}
          getForm={f => setState({ form: f })}
          onReset={handleReset}
        />
      </Col>
    </Row>
  );
};
export default SearchNode;

const shrinkIconStyle: React.CSSProperties = {
  width: 17,
  height: 17,
  marginLeft: -16
};
