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
 * @name ToolTipIcon
 * @author chuxu
 */

import React from 'react';
import { Tooltip } from 'antd';
import { TooltipProps } from 'antd/lib/tooltip';
import styles from './index.less';

interface ToolTipIconProps {
  isToolTip?: boolean;
  toolTipProps?: TooltipProps;
  iconName: string;
  imgStyle?: React.CSSProperties;
  title?: string | React.ReactNode;
}

const ToolTipIcon: React.FC<ToolTipIconProps> = props => {
  return props.isToolTip ? (
    <Tooltip title={props.title} {...props.toolTipProps}>
      <img
        className={!props.imgStyle && styles.actionIcon}
        style={{ ...props.imgStyle }}
        src={require(`../../assets/${props.iconName}.png`)}
      />
    </Tooltip>
  ) : (
    <img
      className={!props.imgStyle && styles.actionIcon}
      style={props.imgStyle}
      src={require(`../../assets/${props.iconName}.png`)}
    />
  );
};
export default ToolTipIcon;

ToolTipIcon.defaultProps = {
  isToolTip: true
};
