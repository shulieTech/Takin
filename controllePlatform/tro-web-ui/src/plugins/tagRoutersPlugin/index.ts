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

export default {};
// import globby from 'globby';
// import { join } from 'path';
// import { readFileSync } from 'fs';
// export default (api, opts) => {
//   const { paths } = api;
//   api.onGenerateFiles(() => {
//     const cwd = paths.absSrcPath;
//     const tpl = join(__dirname, './template/tagRouters.ts');
//     let tplContent = readFileSync(tpl, 'utf-8');
//     const list = globby
//       .sync(`./pages/**/*.{tx,tsx}`, {
//         cwd
//       })
//       .filter(
//         (item: string) =>
//           item.indexOf('/components/') === -1 &&
//           item.indexOf('/utils/') === -1 &&
//           item.indexOf('/stores/') === -1 &&
//           item.indexOf('/index/') === -1 &&
//           item.indexOf('/user/') === -1 &&
//           item.indexOf('/404.tsx') === -1 &&
//           item.indexOf('/models/') === -1 &&
//           item.indexOf('/common/') === -1 &&
//           item.indexOf('/utils.tsx') === -1 &&
//           item.indexOf('/enum.ts') === -1 &&
//           item.indexOf('/context.ts') === -1 &&
//           item.indexOf('/modals/') === -1
//       );
//     tplContent = tplContent.replace(
//       '// <%= tagNavMap %>',
//       `
//         ${list
//           .map((item, index) => {
//             // console.log(item);
//             const name = item
//               .replace('./pages/', '')
//               .replace('/index.tsx', '')
//               .replace('/indexPage.tsx', '')
//               .replace('/indexpage.tsx', '')
//               .replace('page', '')
//               .replace('Page', '')
//               .replace('.tsx', '');
//             const url = item.replace('./', 'src/');
//             const componentName = readFileSync(url, 'utf-8');
//             // return ` '/${name}' : ${componentName},`;
//             return ` '/${name}' : import('${url}'),`;
//           })
//           .join('\n')}
//         `.trim()
//     );
//     api.writeTmpFile('../../tagRouters.ts', tplContent);
//   });
//   api.addRuntimePluginKey('tagRouters');
// };
