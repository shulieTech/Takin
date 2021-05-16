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
 * @name 步骤1-基本信息
 */

import { Icon, Input, InputNumber, Radio, Statistic, Tooltip } from 'antd';
import RadioGroup from 'antd/lib/radio/group';
import { FormDataType } from 'racc/dist/common-form/type';
import React, { useEffect } from 'react';
import { FormCardMultipleDataSourceBean } from 'src/components/form-card-multiple/type';
import { TestMode } from '../enum';
import PressureTestSceneService from '../service';
import styles from './../index.less';
import FixLineCharts from './FixLineCharts';
import InputNumberWithSlider from './InputNumberWithSlider';
import StepCharts from './StepCharts';
import TimeInputWithUnit from './TimeInputWithUnit';

interface Props {}

const PressureConfig = (
  state,
  setState,
  props
): FormCardMultipleDataSourceBean => {
  /** @name 施压配置 */
  const getPressureConfigFormData = (): FormDataType[] => {
    const { location } = props;
    const { query } = location;
    const { action } = query;

    const { detailData, pressureMode, testMode } = state;

    useEffect(() => {
      getEstimateFlow();
      handleStepChartsData(state.stepIncreasingTime, state.pressureTestTime);
    }, [
      state.pressureTestTime,
      state.concurrenceNum,
      state.pressureMode,
      state.lineIncreasingTime,
      state.stepIncreasingTime,
      state.step,
      state.testMode
    ]);

    /**
     * @name 获取预计消耗流量
     */
    const getEstimateFlow = async () => {
      let result = {};
      if (pressureMode === 1) {
        result = {
          concurrenceNum: state.concurrenceNum,
          pressureMode: state.pressureMode,
          pressureTestTime: state.pressureTestTime,
          pressureType: state.testMode
        };
      }
      if (pressureMode === 2) {
        result = {
          concurrenceNum: state.concurrenceNum,
          increasingTime: state.lineIncreasingTime,
          pressureMode: state.pressureMode,
          pressureTestTime: state.pressureTestTime,
          pressureType: state.testMode
        };
      }
      if (pressureMode === 3) {
        result = {
          concurrenceNum: state.concurrenceNum,
          increasingTime: state.stepIncreasingTime,
          pressureMode: state.pressureMode,
          pressureTestTime: state.pressureTestTime,
          step: state.step,
          pressureType: state.testMode
        };
      }
      if (handleCheckIsComplete()) {
        const {
          data: { success, data }
        } = await PressureTestSceneService.getEstimateFlow(result);
        if (success) {
          setState({
            estimateFlow: data.data
          });
        }
      } else {
        setState({
          estimateFlow: null
        });
      }
    };

    const handleChangePressureMode = value => {
      setState({
        pressureMode: value,
        stepIncreasingTime: { time: undefined, unit: 'm' },
        step: null,
        lineIncreasingTime: { time: undefined, unit: 'm' }
      });
      handleCheckIsComplete();
    };

    /**
     * @name 检测施压配置字段是否完整
     */
    const handleCheckIsComplete = () => {
      const data = state.form && state.form.getFieldsValue();
      const concurrenceNum = state.concurrenceNum;
      const ipNum = state.ipNum;
      const pressureTestTime = state.pressureTestTime;
      const lineIncreasingTime = state.lineIncreasingTime;
      const stepIncreasingTime = state.stepIncreasingTime;
      const step = state.step;
      let flag = false;
      const concurrenceNumFlag =
        state.testMode !== TestMode.并发模式 || concurrenceNum;

      // console.log(
      //   concurrenceNum,
      //   ipNum,
      //   pressureTestTime,
      //   lineIncreasingTime,
      //   stepIncreasingTime,
      //   step,
      //   concurrenceNumFlag
      // );
      if (state.pressureMode === 1) {
        if (
          concurrenceNumFlag &&
          ipNum &&
          pressureTestTime &&
          pressureTestTime.time &&
          pressureTestTime.unit
        ) {
          flag = true;
        }
      }
      if (state.pressureMode === 2) {
        if (
          concurrenceNumFlag &&
          ipNum &&
          pressureTestTime &&
          pressureTestTime.time &&
          pressureTestTime.unit &&
          lineIncreasingTime &&
          lineIncreasingTime.time &&
          lineIncreasingTime.unit
        ) {
          flag = true;
        }
      }
      if (state.pressureMode === 3) {
        if (
          concurrenceNumFlag &&
          ipNum &&
          pressureTestTime &&
          pressureTestTime.time &&
          pressureTestTime.unit &&
          stepIncreasingTime &&
          stepIncreasingTime.time &&
          stepIncreasingTime.unit &&
          step
        ) {
          flag = true;
        }
      }
      // console.log(flag);
      return flag;
    };

    /**
     * @name 最大并发数变化
     */
    const handleChangeConcurrenceNum = value => {
      setState({
        concurrenceNum: value
      });
    };

    /**
     * @name 线性递增时长变化
     */
    const handleChangeLineIncreasingTime = value => {
      // console.log(' 线性递增时长变化value', value);
      // setState({ lineIncreasingTime: value });
      handleCompareTime(state.pressureTestTime, value);
    };

    /**
     * @name 阶梯递增时长变化
     */
    const handleChangeStepIncreasingTime = value => {
      // setState({ stepIncreasingTime: value });
      handleCompareTime(state.pressureTestTime, value);
    };

    /**
     * @name 压测时长变化
     */
    const handleChangePressureTestTime = value => {
      setState({ pressureTestTime: value });
    };

    /**
     * @name 递增阶梯变化
     */
    const handleChangeStep = value => {
      setState({ step: value });
    };

    /**
     * @name 最大并发数失去焦点
     */
    const handleBlurConcurrenceNum = async value => {
      if (!value) {
        return;
      }

      const {
        data: { success, data }
      } = await PressureTestSceneService.getMaxMachineNumber({
        concurrenceNum: state.concurrenceNum
      });
      if (success) {
        setState({
          ipNum: data.min,
          minIpNum: data.min,
          maxIpNum: data.max
        });
      }
      handleCheckIsComplete();
      if (state.pressureMode === 3) {
        handleStepChartsData(state.stepIncreasingTime, state.pressureTestTime);
      }
    };

    /**
     * @name 压测时长失去焦点
     */
    const handleBlurPressureTestTime = value => {
      // console.log(value);

      setState({
        pressureTestTime: value,
        indexss: state.indexss + 1
      });

      handleCheckIsComplete();

      if (state.pressureMode === 2) {
        handleCompareTime(value, state.lineIncreasingTime);
      }
      if (state.pressureMode === 3) {
        handleCompareTime(value, state.stepIncreasingTime);
      }
    };

    /**
     * @name 线性递增时长失去焦点
     */
    const handleBlurLineIncreasingTime = value => {
      setState({
        lineIncreasingTime: value,
        indexss: state.indexss + 1
      });

      handleCheckIsComplete();
      handleCompareTime(state.pressureTestTime, value);
    };

    /**
     * @name 阶梯递增时长失去焦点
     */
    const handleBlurStepIncreasingTime = value => {
      setState({
        stepIncreasingTime: value,
        indexss: state.indexss + 1
      });

      handleCheckIsComplete();
      handleStepChartsData(value, state.pressureTestTime);
      handleCompareTime(state.pressureTestTime, value);
    };

    /**
     * @name 阶梯层数失去焦点
     */
    const handleBlurStep = value => {
      handleCheckIsComplete();
      handleStepChartsData(state.stepIncreasingTime, state.pressureTestTime);
    };

    /**
     * @name 计算stepChartsData
     */
    const handleStepChartsData = (value, pressureTestTime) => {
      const midData = [];
      // tslint:disable-next-line:no-increment-decrement
      for (let i = 0; i < state.step; i++) {
        value.unit === 'm'
          ? midData.push([
            (value.time / state.step) * (i + 1),
            ((state.testMode === TestMode.并发模式
                ? state.concurrenceNum
                : state.tpsNum) /
                state.step) *
                (i + 1)
          ])
          : midData.push([
            (value.time / 60 / state.step) * (i + 1),
            ((state.testMode === TestMode.并发模式
                ? state.concurrenceNum
                : state.tpsNum) /
                state.step) *
                (i + 1)
          ]);
      }

      if (handleCheckIsComplete()) {
        if (midData.length > 0) {
          setState({
            stepChartsData: [[0, 0]]
              .concat(midData)
              .concat([
                [
                  pressureTestTime.unit === 'm'
                    ? pressureTestTime.time
                    : pressureTestTime.time / 60,
                  state.testMode === TestMode.并发模式
                    ? state.concurrenceNum
                    : state.tpsNum
                ]
              ])
          });
        }
      } else {
        setState({
          stepChartsData: null,
          estimateFlow: null
        });
      }
    };

    /**
     * @name 比较压测时长和递增时长
     */
    const handleCompareTime = (pressureTestTime, lineIncreasingTime) => {
      // debugger;
      let pt;
      let lt;
      // console.log(
      //   'pressureTestTime, lineIncreasingTime',
      //   pressureTestTime,
      //   lineIncreasingTime
      // );
      if (state.pressureMode === 2) {
        if (
          pressureTestTime &&
          pressureTestTime.time &&
          lineIncreasingTime &&
          lineIncreasingTime.time
        ) {
          if (pressureTestTime.unit === 'm') {
            pt = pressureTestTime.time * 60;
          } else {
            pt = pressureTestTime.time;
          }

          if (state.lineIncreasingTime.unit === 'm') {
            lt = lineIncreasingTime.time * 60;
          } else {
            lt = lineIncreasingTime.time;
          }

          if (pt < lt) {
            setState({
              lineIncreasingTime: pressureTestTime,
              indexss: state.indexss + 1
            });
            state.form.setFieldsValue({
              lineIncreasingTime: pressureTestTime
            });
            // handleChangeLineIncreasingTime(pressureTestTime);
          }
        }
      }
      if (state.pressureMode === 3) {
        if (
          pressureTestTime &&
          pressureTestTime.time &&
          lineIncreasingTime &&
          lineIncreasingTime.time
        ) {
          if (pressureTestTime.unit === 'm') {
            pt = pressureTestTime.time * 60;
          } else {
            pt = pressureTestTime.time;
          }

          if (lineIncreasingTime.unit === 'm') {
            lt = lineIncreasingTime.time * 60;
          } else {
            lt = lineIncreasingTime.time;
          }

          if (pt < lt) {
            setState({
              stepIncreasingTime: pressureTestTime,
              indexss: state.indexss + 1
            });
            state.form.setFieldsValue({
              stepIncreasingTime: pressureTestTime
            });
            handleChangeStepIncreasingTime(pressureTestTime);
            // handleStepChartsData(pressureTestTime, pressureTestTime);
          }
        }
      }
    };

    const handleChangeMode = async (mode: TestMode) => {
      // setState({ testMode: mode });

      setState({
        pressureMode: 1,
        /** 压力模式 */
        testMode: mode,
        // /** 最大并发数 */
        // concurrenceNum: undefined,
        // /** 指定机器IP数 */
        // ipNum: undefined,
        /** 指定机器IP最小数 */
        // minIpNum: 0,
        // /** 指定机器IP最Da数 */
        // maxIpNum: undefined,
        /** 压测时长 */
        pressureTestTime: { time: undefined, unit: 'm' },
        /** 递增时长（线性） */
        lineIncreasingTime: { time: undefined, unit: 'm' },
        /** 递增时长(阶梯) */
        stepIncreasingTime: { time: undefined, unit: 'm' },
        /** 阶梯层数 */
        step: undefined,
        stepChartsData: null,
        flag: false,
        indexss: 0,
        estimateFlow: null
      });
      state.form.setFieldsValue({
        pressureMode: 1,
        pressureTestTime: { time: undefined, unit: 'm' }
      });

      if (mode === TestMode.自定义模式) {
        return;
      }
      let params = null;
      if (mode === TestMode.并发模式) {
        params = state.concurrenceNum
          ? { concurrenceNum: state.concurrenceNum }
          : null;
      }
      if (mode === TestMode.TPS模式) {
        const list = state.businessList;
        if (!list) {
          return;
        }
        const isEmpty = list.find(item => !item.targetTPS);
        if (isEmpty) {
          return;
        }
        let tpsNum = 0;
        list.forEach(item => {
          tpsNum += item.targetTPS;
        });
        params = { tpsNum };
      }
      if (!params) {
        return;
      }
      const {
        data: { success, data }
      } = await PressureTestSceneService.getMaxMachineNumber(params);
      if (success) {
        setState({
          ...params,
          ipNum: data.min,
          minIpNum: data.min,
          maxIpNum: data.max
        });
      }
    };

    const basicFormData: FormDataType[] = [
      {
        key: 'pressureType',
        label: (
          <span style={{ fontSize: 14 }}>
            压力模式
            <Tooltip
              title="自定义模式是指所有配置都读区jmeter脚本里面的参数"
              placement="right"
              trigger="click"
            >
              <Icon style={{ marginLeft: 4 }} type="question-circle" />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue: action !== 'add' ? detailData.pressureType : testMode,
          rules: [{ required: true, message: '请选择压力模式' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <RadioGroup
            onChange={e => handleChangeMode(e.target.value)}
            options={[
              { label: '并发模式', value: TestMode.并发模式 },
              { label: 'TPS模式', value: TestMode.TPS模式 }
              // { label: '自定义模式', value: TestMode.自定义模式 }
            ]}
          />
        )
      }
    ];

    const commonFormData: FormDataType[] = [
      {
        key: 'ipNum',
        label: (
          <span style={{ fontSize: 14 }}>
            指定Pod数
            <Tooltip
              title="指定压力机pod数量，可参考建议值范围。指定数量过高可能导致硬件资源无法支持；指定数量过低可能导致发起压力达不到要求"
              placement="right"
              trigger="click"
            >
              <Icon type="question-circle" style={{ marginLeft: 4 }} />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue: action !== 'add' ? detailData.ipNum : state.ipNum,
          rules: [{ required: true, message: '请输入指定Pod数' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <Input
            min={1}
            addonAfter={`建议Pod数：${
              !state.minIpNum ? '-' : `${state.minIpNum}-${state.maxIpNum}`
            }`}
            onBlur={handleCheckIsComplete}
          />
          // <InputNumberWithSlider
          //   disabled={false}
          //   min={state.minIpNum}
          //   max={state.maxIpNum}
          //   onBlur={handleCheckIsComplete}
          // />
        ),
        extra: (
          <div
            className={styles.chartWrap}
            style={{ top: testMode === TestMode.并发模式 ? -60 : -40 }}
          >
            <p className={styles.title}>
              流量预估
              <Tooltip
                title="流量预估是根据施压配置参数模拟的压力图与预计消耗流量，最终计费以实际施压情况为准"
                placement="right"
                trigger="click"
              >
                <Icon type="question-circle" style={{ marginLeft: 4 }} />
              </Tooltip>
              <span className={styles.subTitle}>预计消耗：</span>
              {state.estimateFlow ? (
                <span className={styles.subTitleNum}>
                  <Statistic
                    precision={2}
                    suffix="vum"
                    value={state.estimateFlow}
                  />
                </span>
              ) : (
                '-- vum'
              )}
            </p>
            {handleCheckIsComplete() ? (
              <div>
                {state.pressureMode === 1 && (
                  <FixLineCharts
                    chartsInfo={[
                      [
                        0,
                        state.testMode === TestMode.并发模式
                          ? state.concurrenceNum
                          : state.tpsNum
                      ],
                      [
                        state.pressureTestTime && state.pressureTestTime.time,
                        state.testMode === TestMode.并发模式
                          ? state.concurrenceNum
                          : state.tpsNum
                      ]
                    ]}
                  />
                )}
                {state.pressureMode === 2 && (
                  <FixLineCharts
                    chartsInfo={[
                      [0, 0],
                      [
                        state.lineIncreasingTime &&
                        state.lineIncreasingTime.unit === 'm'
                          ? state.lineIncreasingTime.time
                          : state.lineIncreasingTime.time / 60,
                        state.testMode === TestMode.并发模式
                          ? state.concurrenceNum
                          : state.tpsNum
                      ],
                      [
                        state.pressureTestTime &&
                        state.pressureTestTime.unit === 'm'
                          ? state.pressureTestTime.time
                          : state.pressureTestTime.time / 60,
                        // state.pressureTestTime && state.pressureTestTime.time,
                        state.testMode === TestMode.并发模式
                          ? state.concurrenceNum
                          : state.tpsNum
                      ]
                    ]}
                  />
                )}
                {state.pressureMode === 3 && (
                  <StepCharts chartsInfo={state.stepChartsData} />
                )}
              </div>
            ) : (
              <div>左侧数据填写完整后，展示施压趋势图</div>
            )}
          </div>
        )
      },
      {
        key: 'pressureTestTime',
        label: (
          <span style={{ fontSize: 14 }}>
            压测时长
            <Tooltip
              title="为更好地测试系统性能，建议压测时长不低于2分钟，压测开始后可随时手动停止。"
              placement="right"
              trigger="click"
            >
              <Icon type="question-circle" style={{ marginLeft: 4 }} />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue:
            action !== 'add' ? state.pressureTestTime : state.pressureTestTime,
          rules: [{ required: true, message: '请输入压测时长' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <TimeInputWithUnit
            isReload={state.indexss}
            onBlur={handleBlurPressureTestTime}
            // onChange={handleChangePressureTestTime}
          />
        )
      },
      {
        key: 'pressureMode',
        label: (
          <span style={{ fontSize: 14 }}>
            施压模式
            <Tooltip
              title={`
            1.固定压力值：压力机将会全程保持最大并发量进行施压；
            2.线性递增：压力机将会以固定速率增压，直至达到最大并发量；\n
            3.阶梯递增：压力机将会以固定周期增压，每次增压后保持一段时间，直至达到最大并发量；
            `}
              placement="right"
              trigger="click"
            >
              <Icon type="question-circle" style={{ marginLeft: 4 }} />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue:
            action !== 'add' ? detailData.pressureMode : pressureMode,
          rules: [{ required: true, message: '请选择施压模式' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <Radio.Group onChange={e => handleChangePressureMode(e.target.value)}>
            <Radio value={1}>固定压力值</Radio>
            <Radio value={2}>线性递增</Radio>
            <Radio value={3}>阶梯递增</Radio>
          </Radio.Group>
        )
      }
    ];

    const concurrenceFormData: FormDataType[] = [
      {
        key: 'concurrenceNum',
        label: (
          <span style={{ fontSize: 14 }}>
            最大并发
            <Tooltip
              title="压测场景最终会达到的最大并发量。"
              placement="right"
              trigger="click"
            >
              <Icon style={{ marginLeft: 4 }} type="question-circle" />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue:
            action !== 'add' ? detailData.concurrenceNum : undefined,
          rules: [{ required: true, message: '请输入最大并发数量' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <InputNumber
            min={1}
            max={100000}
            placeholder="请输入1~100,000之间的正整数"
            onBlur={e => handleBlurConcurrenceNum(e.target.value)}
            onChange={value => handleChangeConcurrenceNum(value)}
          />
        )
      }
    ];

    const lineFormData = [
      {
        key: 'lineIncreasingTime',
        label: (
          <span style={{ fontSize: 14 }}>
            递增时长
            <Tooltip
              title="增压直至最大并发量的时间"
              placement="right"
              trigger="click"
            >
              <Icon type="question-circle" style={{ marginLeft: 4 }} />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue: state.lineIncreasingTime,
          rules: [{ required: true, message: '请输入递增时长' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <TimeInputWithUnit
            onBlur={handleBlurLineIncreasingTime}
            isReload={state.indexss}
            // onChange={handleChangeLineIncreasingTime}
            // selectDisabled={
            //   state.pressureTestTime && state.pressureTestTime.unit === 's'
            //     ? true
            //     : false
            // }
          />
        )
      }
    ];

    const stepFormData = [
      {
        key: 'stepIncreasingTime',
        label: (
          <span style={{ fontSize: 14 }}>
            递增时长
            <Tooltip
              title="增压直至最大并发量的时间"
              placement="right"
              trigger="click"
            >
              <Icon type="question-circle" style={{ marginLeft: 4 }} />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue: state.stepIncreasingTime,
          rules: [{ required: true, message: '请输入递增时长' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <TimeInputWithUnit
            onBlur={handleBlurStepIncreasingTime}
            isReload={state.indexss}
          />
        )
      },
      {
        key: 'step',
        label: (
          <span style={{ fontSize: 14 }}>
            阶梯层数
            <Tooltip
              title="并发量从0周期增加到最大并发量的次数"
              placement="right"
              trigger="click"
            >
              <Icon type="question-circle" style={{ marginLeft: 4 }} />
            </Tooltip>
          </span>
        ),
        options: {
          initialValue: action !== 'add' ? state.step : state.step,
          rules: [{ required: true, message: '请输入阶梯层数' }]
        },
        formItemProps: { labelCol: { span: 4 }, wrapperCol: { span: 13 } },
        node: (
          <InputNumber
            placeholder="请输入1~100的整数"
            min={1}
            max={100}
            onChange={handleChangeStep}
            onBlur={handleBlurStep}
          />
        )
      }
    ];

    let formData = [...basicFormData];

    /** @name 根据压力模式渲染 */
    if (testMode === TestMode.并发模式) {
      formData = [...formData, ...concurrenceFormData, ...commonFormData];
    }

    if (testMode === TestMode.TPS模式) {
      formData = [...formData, ...commonFormData];
    }

    /** @name 根据施压模式渲染 */
    if (testMode !== TestMode.自定义模式) {
      if (pressureMode === 2) {
        formData = formData.concat(lineFormData);
      }
      if (pressureMode === 3) {
        formData = formData.concat(stepFormData);
      }
    }

    return formData;
  };

  return {
    title: '施压配置',
    rowNum: 1,
    span: 14,
    formData: getPressureConfigFormData()
  };
};

export default PressureConfig;
