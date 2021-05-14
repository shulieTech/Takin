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
 * @name 通用表达卡
 * @author
 */
import React, { useEffect, Fragment } from 'react';
import { FormCardProps } from './type';
import { Row, Col, Form, Card } from 'antd';
import { CommonForm } from 'racc';
import TitleComponent from 'src/common/title';
import Styles from './index.less';

const CustomFormCard: React.FC<FormCardProps> = props => {
  useEffect(() => {
    if (props.getForm) {
      props.getForm(props.form);
    }
  }, []);

  return (
    <div>
      {props.dataSource.map((item, index) => (
        <Fragment key={index}>
          <p className={Styles.title}>{item.title}</p>
          <Row type="flex" justify="start" style={{}}>
            <Col key={index} span={24} pull={0}>
              <CommonForm
                formItemProps={{
                  labelCol: { span: 0 },
                  wrapperCol: { span: 24 }
                }}
                rowNum={2}
                btnProps={{ isResetBtn: false, isSubmitBtn: false }}
                {...(props.commonFormProps && props.commonFormProps)}
                {...(item.commonFormProps && item.commonFormProps)}
                form={props.form}
                formData={item.formData}
              />
            </Col>
          </Row>
        </Fragment>
      ))}
    </div>
  );
};

export default Form.create<FormCardProps>()(CustomFormCard);
