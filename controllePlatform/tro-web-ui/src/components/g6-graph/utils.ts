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

export const getLabelMaxLength = label => {
  const enLength = getLabelCnNum(label);
  let result = 0;
  if (!enLength) {
    result = 24;
  } else if (enLength < 6) {
    result = 16;
  } else {
    result = 10;
  }
  return result;
};

export const getLabelCnNum = label => {
  let len = 0;
  if (!label) {
    return 0;
  }
  for (let i = 0; i < label.length; i = i + 1) {
    if (label.charCodeAt(i) > 127 || label.charCodeAt(i) === 94) {
      len += 1;
    }
  }
  return len;
};

export const getEdgeType = item => {
  if (item.source === item.target) {
    return 'loop';
  }
  return 'polyline';
  // return 'quadratic-label-edge';
};

export const getOffset = ({ id, source, target, edges }) => {
  const repeatList = edges.filter(
    item => item.source === source && item.target === target
  );
  /** @name 没有重复，判断是否双向调用 */
  if (repeatList.length === 1) {
    const isBoth = edges.find(
      item => item.source === target && item.target === source
    );
    if (isBoth) {
      return 50;
      // return 80;
    }
    return 0;
  }
  /** @name 重复，初始值80，累加140 */
  const index = repeatList.findIndex(item => item.id === id);
  return index * 90 + 100;
  // return index * 180 + 100;
};
