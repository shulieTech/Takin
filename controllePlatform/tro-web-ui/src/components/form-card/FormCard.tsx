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
import { Row, Col, Form } from 'antd';
import { CommonForm } from 'racc';
import TitleComponent from 'src/common/title';

const FormCard: React.FC<FormCardProps> = props => {
  useEffect(() => {
    if (props.getForm) {
      props.getForm(props.form);
    }
  }, []);
  return (
    <div>
      {props.dataSource.map((item, index) => (
        <Fragment>
          <div>
            {(item.title || item.extra) && (
              <TitleComponent content={item.title} extra={item.extra} />
            )}
          </div>
          <Row type="flex" justify="start">
            <Col key={index} span={23} pull={1}>
              <CommonForm
                formItemProps={{
                  labelCol: { span: 8 },
                  wrapperCol: { span: 15 }
                }}
                rowNum={2}
                {...props.commonFormProps}
                btnProps={{ isResetBtn: false, isSubmitBtn: false }}
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

export default Form.create<FormCardProps>()(FormCard);
