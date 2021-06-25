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

import React, { Fragment, useEffect } from 'react';
import { useStateReducer } from 'racc';
import LinkMarkService from '../service';

import styles from './../index.less';
import Link from 'umi/link';

interface Props {}
export interface LinkCardState {
  isReload?: boolean;
  linkData?: any;
}
const LinkCard: React.FC<Props> = props => {
  const [state, setState] = useStateReducer<LinkCardState>({
    isReload: false,
    linkData: null
  });

  useEffect(() => {
    queryMiddleware();
  }, [state.isReload]);

  /**
   * @name 获取链路统计数据
   */
  const queryMiddleware = async () => {
    const {
      data: { success, data }
    } = await LinkMarkService.queryLinkStatistic({});
    if (success) {
      setState({
        linkData: data
      });
    }
  };

  const { linkData } = state;
  const cardData = [
    {
      title: '接入业务流程',
      number: linkData && linkData.businessProcessCount,
      link: '/businessFlow',
      icon: true
    },
    {
      title: '覆盖业务活动',
      number: linkData && linkData.businessActiveCount,
      link: '/businessActivity',
      icon: true
    },
    {
      title: '覆盖系统流程',
      number: linkData && linkData.systemProcessCount,
      link: '/systemFlow',
      icon: true
    },
    {
      title: '应用接入情况',
      number: linkData && linkData.onLineApplicationCount,
      icon: false
    },
    {
      title: '系统流程变更',
      number: linkData && linkData.systemChangeCount,
      link: '/systemFlow?ischange=1',
      icon: true
    }
  ];

  return (
    <div className={styles.linkCardWrap}>
      {cardData.map((item, k) => {
        return item.link ? (
          <Link
            className={styles.cardWrap}
            style={{ display: 'inline-block' }}
            to={item.link}
            key={k}
          >
            <div key={k}>
              <p className={styles.title}>{item.title}</p>
              <p className={styles.cardNum}>
                {item.number}
                {item.icon ? (
                  <img
                    style={{ width: 16, marginLeft: 20 }}
                    src={require('./../../../assets/sanjiao.png')}
                  />
                ) : (
                  <img
                    style={{ width: 16, marginLeft: 20 }}
                    src={require('./../../../assets/sanjiao2.png')}
                  />
                )}
              </p>
              <img
                style={{
                  width: 98,
                  position: 'absolute',
                  top: 0,
                  right: 0,
                  zIndex: 0
                }}
                src={require('./../../../assets/link_card_bg.png')}
              />
            </div>
          </Link>
        ) : (
          <div key={k} className={styles.cardWrap}>
            <p className={styles.title}>{item.title}</p>
            <p className={styles.cardNum}>
              {item.number}
              {item.icon ? (
                <img
                  style={{ width: 16, marginLeft: 20 }}
                  src={require('./../../../assets/sanjiao.png')}
                />
              ) : (
                <img
                  style={{ width: 16, marginLeft: 20 }}
                  src={require('./../../../assets/sanjiao2.png')}
                />
              )}
            </p>
            <img
              style={{
                width: 98,
                position: 'absolute',
                top: 0,
                right: 0,
                zIndex: 0
              }}
              src={require('./../../../assets/link_card_bg.png')}
            />
          </div>
        );
      })}
    </div>
  );
};
export default LinkCard;
