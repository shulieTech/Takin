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

import { Graph } from '@antv/g6';
import { Button, message, Popconfirm } from 'antd';
import { ColumnProps } from 'antd/lib/table';
import { CommonTabs, defaultColumnProps } from 'racc';
import React, { Fragment } from 'react';
import { ActivityBean, NodeBean, NodeType } from '../enum';
import BusinessActivityService from '../service';
import {
  NodeDetailsCard,
  NodeDetailsCollapse,
  NodeDetailsCustomTable,
  NodeDetailsHeader,
  NodeDetailsTab,
  NodeDetailsTable,
  OuterService
} from './NodeInfoCommonNode';

export const RenderNodeInfoByType = (
  { nodeInfo, details, graph },
  setState: any
) => {
  let renderNode: React.ReactNode = null;
  if (!nodeInfo || !nodeInfo) {
    return null;
  }
  switch (nodeInfo.nodeType) {
    /**
     * @name 应用
     */
    case NodeType.应用:
      renderNode = renderApp(nodeInfo);
      break;
    /**
     * @name 数据库
     */
    case NodeType.数据库:
      renderNode = renderDb(nodeInfo);
      break;
    /**
     * @name 缓存
     */
    case NodeType.缓存:
      renderNode = renderCache(nodeInfo);
      break;
    /**
     * @name 消息队列
     */
    case NodeType.消息队列:
      renderNode = renderMQ(nodeInfo);
      break;
    /**
     * @name 文件
     */
    case NodeType.文件:
      renderNode = renderOSS(nodeInfo);
      break;
    /**
     * @name 外部应用
     */
    case NodeType.外部应用:
      renderNode = renderOuterApp(nodeInfo);
      break;
    /**
     * @name 未知应用
     */
    case NodeType.未知应用:
      renderNode = renderUnknowApp(nodeInfo, details, graph, setState);
      break;
    default:
      break;
  }
  return renderNode;
};

/**
 * @name ==========================================应用==================================================
 */
const DownStream: React.FC<NodeBean> = props => {
  const callService = props.callService;
  if (!callService) {
    return null;
  }
  return (
    <Fragment>
      {callService.map(item => (
        <NodeDetailsCollapse title={item.label}>
          <NodeDetailsCustomTable dataSource={item.dataSource} />
        </NodeDetailsCollapse>
      ))}
    </Fragment>
  );
};
const renderApp = (nodeInfo: NodeBean) => {
  const dataSource = [
    {
      tab: '对外服务',
      component: <OuterService {...nodeInfo} />
    },
    {
      tab: '下游',
      component: <DownStream {...nodeInfo} />
    },
    {
      tab: '详情',
      component: <NodeDetailsCard {...nodeInfo} />
    }
  ];
  const details = [
    {
      label: '对外服务',
      value: nodeInfo.providerService && nodeInfo.providerService.length
    },
    {
      label: '下游节点',
      value: nodeInfo.callService && nodeInfo.callService.length
    },
    {
      label: '应用负责人',
      value: nodeInfo.manager
    }
  ];
  return (
    <Fragment>
      <NodeDetailsHeader
        type={nodeInfo.nodeType}
        title={nodeInfo._label}
        details={details}
      />
      <NodeDetailsTab
        dataSource={dataSource}
        onRender={(item, index) => (
          <CommonTabs.TabPane tab={item.tab} key={index.toString()}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};

/**
 * @name ==========================================数据库==================================================
 */
const TableList: React.FC<NodeBean> = props => {
  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...defaultColumnProps,
        title: '表名称',
        dataIndex: ActivityBean.表名称
      }
    ];
  };
  return <NodeDetailsTable columns={getColumns()} dataSource={props.db} />;
};
const renderDb = (nodeInfo: NodeBean) => {
  const dataSource = [
    {
      tab: '表',
      component: <TableList {...nodeInfo} />
    },
    {
      tab: '详情',
      component: <NodeDetailsCard {...nodeInfo} />
    }
  ];
  const details = [
    {
      label: '表',
      value: nodeInfo.db && nodeInfo.db.length
    }
  ];
  return (
    <Fragment>
      <NodeDetailsHeader
        type={nodeInfo.nodeType}
        title={nodeInfo._label}
        details={details}
      />
      <NodeDetailsTab
        dataSource={dataSource}
        onRender={(item, index) => (
          <CommonTabs.TabPane tab={item.tab} key={index.toString()}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};

/**
 * @name ==========================================缓存==================================================
 */
const renderCache = (nodeInfo: NodeBean) => {
  const dataSource = [
    {
      tab: '详情',
      component: <NodeDetailsCard {...nodeInfo} />
    }
  ];
  const details = [
    {
      label: '节点',
      value: nodeInfo.nodes && nodeInfo.nodes.length
    }
  ];
  return (
    <Fragment>
      <NodeDetailsHeader
        type={nodeInfo.nodeType}
        title={nodeInfo._label}
        details={details}
      />
      <NodeDetailsTab
        dataSource={dataSource}
        onRender={(item, index) => (
          <CommonTabs.TabPane tab={item.tab} key={index.toString()}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};

/**
 * @name ==========================================消息队列==================================================
 */
const TopicList: React.FC<NodeBean> = props => {
  const getColumns = (): ColumnProps<any>[] => {
    return [
      {
        ...defaultColumnProps,
        title: 'Topic',
        dataIndex: ActivityBean.Topic
      }
    ];
  };
  return <NodeDetailsTable columns={getColumns()} dataSource={props.mq} />;
};
const renderMQ = (nodeInfo: NodeBean) => {
  const dataSource = [
    {
      tab: 'Topic',
      component: <TopicList {...nodeInfo} />
    },
    {
      tab: '详情',
      component: <NodeDetailsCard {...nodeInfo} />
    }
  ];
  const details = [
    {
      label: 'Topic',
      value: nodeInfo.mq && nodeInfo.mq.length
    }
  ];
  return (
    <Fragment>
      <NodeDetailsHeader
        type={nodeInfo.nodeType}
        title={nodeInfo._label}
        details={details}
      />
      <NodeDetailsTab
        dataSource={dataSource}
        onRender={(item, index) => (
          <CommonTabs.TabPane tab={item.tab} key={index.toString()}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};

/**
 * @name ==========================================文件OSS==================================================
 */
const OSSList: React.FC<NodeBean> = props => {
  const getColumns = (): ColumnProps<any>[] => {
    return [
      // {
      //   ...defaultColumnProps,
      //   title: '文件名称',
      //   dataIndex: ActivityBean.文件名称
      // },
      {
        ...defaultColumnProps,
        title: '文件路径',
        dataIndex: ActivityBean.文件路径
      }
    ];
  };
  return <NodeDetailsTable columns={getColumns()} dataSource={props.oss} />;
};
const renderOSS = (nodeInfo: NodeBean) => {
  const dataSource = [
    {
      tab: '路径',
      component: <OSSList {...nodeInfo} />
    },
    {
      tab: '详情',
      component: <NodeDetailsCard {...nodeInfo} />
    }
  ];
  const details = [
    {
      label: '路径',
      value: nodeInfo.oss && nodeInfo.oss.length
    }
  ];
  return (
    <Fragment>
      <NodeDetailsHeader
        type={nodeInfo.nodeType}
        title={nodeInfo._label}
        details={details}
      />
      <NodeDetailsTab
        dataSource={dataSource}
        onRender={(item, index) => (
          <CommonTabs.TabPane tab={item.tab} key={index.toString()}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};

/**
 * @name ==========================================外部应用==================================================
 */
const renderOuterApp = (nodeInfo: NodeBean) => {
  const dataSource = [
    {
      tab: '对外服务',
      component: <OuterService {...nodeInfo} />
    },
    {
      tab: '详情',
      component: <NodeDetailsCard {...nodeInfo} />
    }
  ];
  const details = [
    {
      label: '对外服务',
      value: nodeInfo.providerService && nodeInfo.providerService.length
    }
  ];
  return (
    <Fragment>
      <NodeDetailsHeader
        type={nodeInfo.nodeType}
        title={nodeInfo._label}
        details={details}
      />
      <NodeDetailsTab
        dataSource={dataSource}
        onRender={(item, index) => (
          <CommonTabs.TabPane tab={item.tab} key={index.toString()}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};
/**
 * @name ==========================================未知应用==================================================
 */
const renderUnknowApp = (
  nodeInfo: NodeBean,
  activityDetails: any,
  graph: Graph,
  setState: any
) => {
  const dataSource = [
    {
      tab: '对外服务',
      component: <OuterService {...nodeInfo} />
    },
    {
      tab: '详情',
      component: <NodeDetailsCard {...nodeInfo} />
    }
  ];
  const details = [
    {
      label: '对外服务',
      value: nodeInfo.providerService && nodeInfo.providerService.length
    }
  ];
  /** @name 标记为外部应用 */
  const handleMarkOuterApp = async () => {
    const {
      data: { success }
    } = await BusinessActivityService.markOuterApp({
      ...activityDetails,
      nodeId: nodeInfo.id
    });
    if (success) {
      message.success('成功标记为外部应用');
      const newNodeInfo = {
        ...nodeInfo,
        label: '外部应用\n',
        _label: '外部应用',
        nodeType: NodeType.外部应用,
        img: require(`../../../assets/outer_icon.png`)
      };
      graph.updateItem(nodeInfo.id, newNodeInfo);
      setState({ nodeInfo: newNodeInfo, reload: true });
    }
  };
  const actions: React.ReactNode = (
    <Popconfirm
      onConfirm={handleMarkOuterApp}
      placement="leftBottom"
      title="确定标记为外部应用吗?"
    >
      <Button style={{ border: '1px solid #11BBD5', fontWeight: 500 }}>
        标记为外部应用
      </Button>
    </Popconfirm>
  );

  return (
    <Fragment>
      <NodeDetailsHeader
        type={nodeInfo.nodeType}
        actions={actions}
        title={nodeInfo._label}
        details={details}
      />
      <div
        className="mg-b2x"
        style={{
          color: '#646464',
          fontWeight: 500,
          lineHeight: '18px',
          fontSize: 13
        }}
      >
        确认该服务为外部服务或内部应用提供，若为外部服务请讲其标记为外部服务；若为内部应用请将其接入探针
      </div>
      <NodeDetailsTab
        dataSource={dataSource}
        onRender={(item, index) => (
          <CommonTabs.TabPane tab={item.tab} key={index.toString()}>
            {item.component}
          </CommonTabs.TabPane>
        )}
      />
    </Fragment>
  );
};
